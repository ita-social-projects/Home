package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_INVITATION_DELETED_BY_ADMIN_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_INVITATION_OVERDUE_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_INVITATION_FORMAT;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_REGISTRATION_TOKEN_MESSAGE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.QInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.MailableService;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.ownership.OwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Primary
public class InvitationServiceImpl implements InvitationService<Invitation, InvitationDto>, MailableService {

    protected final InvitationRepository invitationRepository;

    protected final ServiceMapper mapper;

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    OwnershipService ownershipService;

    @Autowired
    UserCooperationService userCooperationService;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        return null;
    }

    @Override
    public List<InvitationDto> getAllUnsentLetters() {
        List<Invitation> allNotSentInvitations = invitationRepository
            .findAllBySentDatetimeIsNullAndStatusEqualsAndEnabledEquals(
                InvitationStatus.PENDING, true);
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, InvitationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public void markInvitationsAsOverdue() {
        QInvitation qInvitation = QInvitation.invitation;
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        List<Invitation> overdueApartmentInvitations = query.select(qInvitation).from(qInvitation)
            .where((qInvitation.status.eq(InvitationStatus.PENDING))
                    .or(qInvitation.status.eq(InvitationStatus.PROCESSING)),
                qInvitation.requestEndTime.before(LocalDateTime.now())).fetch();
        overdueApartmentInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.OVERDUE);
            invitationRepository.save(invitation);
        });

    }

    @Override
    public void updateSentDateTimeAndStatus(Long id) {
        Invitation invitation = findInvitationById(id);
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setSentDatetime(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
        new InvitationException(String.format(NOT_FOUND_INVITATION_FORMAT, id)));
    }

    @Override
    public void acceptUserInvitation(Invitation invitation) {
        if (invitation.getType().equals(InvitationType.APARTMENT)
        ) {
            Ownership ownership = ownershipService.createOwnership((ApartmentInvitation) invitation);
            userCooperationService.createUserCooperationForOwnership(ownership);
            invitation.setStatus(InvitationStatus.ACCEPTED);
            invitationRepository.save(invitation);
        } else if (invitation.getType().equals(InvitationType.COOPERATION)) {
            userCooperationService.createUserCooperationViaInvitation((CooperationInvitation) invitation);
            invitation.setStatus(InvitationStatus.ACCEPTED);
            invitationRepository.save(invitation);
        }
    }

    @Override
    public void registerWithRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_REGISTRATION_TOKEN_MESSAGE));
        validateInvitation(invitation);
        acceptUserInvitation(invitation);
    }

    @Override
    public void deactivateInvitation(Long id) {
        Invitation anyInvitation = invitationRepository.findById(id)
            .filter(invitation -> (invitation.getSentDatetime() != null
                || invitation.getStatus().equals(InvitationStatus.PENDING))
                && invitation.getEnabled().equals(true))
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_INVITATION_FORMAT, id)));
        anyInvitation.setEnabled(false);
        invitationRepository.save(anyInvitation);
    }

    @Override
    public InvitationDto findInvitationByRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_REGISTRATION_TOKEN_MESSAGE));
        return mapper.convert(invitation, InvitationDto.class);
    }

    @Transactional
    @Override
    public Page<InvitationDto> findAll(Integer pageNumber,
                                       Integer pageSize,
                                       Specification<Invitation> specification) {
        Specification<Invitation> invitationSpecification = specification
            .and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("enabled"), true));
        Page<Invitation> pageCooperationInvitation = invitationRepository
            .findAll(invitationSpecification, PageRequest.of(pageNumber - 1, pageSize));

        return new PageImpl<>(pageCooperationInvitation.getContent().stream()
            .map(this::getInvitationDto)
            .collect(Collectors.toList()));
    }

    private InvitationDto getInvitationDto(Invitation invitation) {
        if (invitation instanceof ApartmentInvitation) {
            ApartmentInvitationDto converted =
                mapper.convert(invitation, ApartmentInvitationDto.class);

            converted.setAddress(((ApartmentInvitation) invitation).getApartment().getHouse().getAddress());
            converted.setHouseId(((ApartmentInvitation) invitation).getApartment().getHouse().getId());

            return converted;

        } else if (invitation instanceof CooperationInvitation) {
            return mapper.convert(invitation, CooperationInvitationDto.class);
        }

        return mapper.convert(invitation, InvitationDto.class);
    }

    private void validateInvitation(Invitation invitation) {
        if (invitation.getStatus().equals(InvitationStatus.OVERDUE)) {
            throw new NotAcceptableHomeException(ALERT_INVITATION_OVERDUE_MESSAGE);
        }
        if (!invitation.getEnabled().equals(true)) {
            throw new NotAcceptableHomeException(ALERT_INVITATION_DELETED_BY_ADMIN_MESSAGE);
        }
    }
}
