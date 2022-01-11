package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

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
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableInvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.ownership.OwnershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
public class InvitationServiceImpl implements InvitationService<Invitation, InvitationDto> {

    private static final String NOT_FOUND_INVITATION_FORMAT = "Invitation with id: %d not found.";

    protected final InvitationRepository invitationRepository;

    @Autowired
    OwnershipService ownershipService;

    @Autowired
    UserCooperationService userCooperationService;

    @PersistenceContext
    protected EntityManager entityManager;

    protected final ServiceMapper mapper;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        return null;
    }

    @Override
    public List<InvitationDto> getAllActiveInvitations() {
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
                new InvitationException("Invitation with id " + id + " was not found"));
    }


    @Override
    public void registerWithRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
                .orElseThrow(() -> new NotFoundHomeException("Registration token not found"));
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
    public InvitationDto findInvitationByRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
                .orElseThrow(() -> new NotFoundHomeException("Registration token not found"));
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

        return pageCooperationInvitation.map(invitation -> mapper.convert(invitation, InvitationDto.class));
    }

    private void validateInvitation(Invitation invitation) {
        if (invitation.getStatus().equals(InvitationStatus.OVERDUE)) {
            throw new NotAcceptableInvitationException("Invitation was overdue");
        }
        if (!invitation.getEnabled().equals(true)) {
            throw new NotAcceptableInvitationException("Invitation was deleted By Cooperation Admin");
        }
    }
}
