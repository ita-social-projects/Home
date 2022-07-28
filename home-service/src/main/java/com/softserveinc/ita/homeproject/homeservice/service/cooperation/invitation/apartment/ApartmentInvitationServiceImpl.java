package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_INVITATION_ALREADY_EXIST_APARTMENT_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_INVITATION_NOT_ACTIVE_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_APARTMENT_ID_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_REGISTRATION_TOKEN_MESSAGE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasterxml.uuid.Generators;
import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.QApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.MailableService;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.ownership.OwnershipService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ApartmentInvitationServiceImpl
    implements InvitationService<ApartmentInvitation, ApartmentInvitationDto>, MailableService {

    private final ServiceMapper mapper;

    @PersistenceContext
    protected EntityManager entityManager;

    private static final int EXPIRATION_TERM = 7;

    protected final InvitationRepository invitationRepository;

    private final ApartmentInvitationRepository apartmentInvitationRepository;

    private final InvitationService<Invitation, InvitationDto> invitationService;

    private final ApartmentRepository apartmentRepository;

    private final OwnershipService ownershipService;

    private final UserCooperationService userCooperationService;

    public ApartmentInvitationServiceImpl(
        ServiceMapper mapper,
        ApartmentInvitationRepository apartmentInvitationRepository,
        ApartmentRepository apartmentRepository,
        EntityManager entityManager,
        InvitationRepository invitationRepository,
        InvitationService<Invitation, InvitationDto> invitationService,
        OwnershipService ownershipService,
        UserCooperationService userCooperationService) {
        this.apartmentInvitationRepository = apartmentInvitationRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
        this.apartmentRepository = apartmentRepository;
        this.invitationRepository = invitationRepository;
        this.invitationService = invitationService;
        this.ownershipService = ownershipService;
        this.userCooperationService = userCooperationService;
    }

    @Override
    public ApartmentInvitationDto createInvitation(ApartmentInvitationDto apartmentInvitationDto) {
        ApartmentInvitation apartmentInvitation = mapper
            .convert(apartmentInvitationDto, ApartmentInvitation.class);
        apartmentInvitation.setStatus(InvitationStatus.PENDING);
        apartmentInvitation.setRegistrationToken(Generators.timeBasedGenerator().generate().toString());
        apartmentInvitation.setRequestEndTime(LocalDateTime.now().plusDays(EXPIRATION_TERM));
        apartmentInvitation.setEnabled(true);

        Long apartmentId = mapper.convert(apartmentInvitation, ApartmentInvitationDto.class).getApartmentId();
        apartmentInvitation.setApartment(apartmentRepository
            .findById(apartmentId)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_APARTMENT_ID_MESSAGE, apartmentId))));


        if (isApartmentInvitationNonExists(apartmentInvitationDto.getEmail(), apartmentId)) {
            invitationRepository.save(apartmentInvitation);
            return mapper.convert(apartmentInvitation, ApartmentInvitationDto.class);
        }
        throw new BadRequestHomeException(ALERT_INVITATION_ALREADY_EXIST_APARTMENT_MESSAGE);
    }

    @Override
    public void acceptUserInvitation(ApartmentInvitation apartmentInvitation) {
        Ownership ownership = ownershipService.createOwnership(apartmentInvitation);
        userCooperationService.createUserCooperationForOwnership(ownership);
        apartmentInvitation.setStatus(InvitationStatus.ACCEPTED);
        apartmentInvitationRepository.save(apartmentInvitation);
    }

    @Override
    public void registerWithRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_REGISTRATION_TOKEN_MESSAGE));
        ApartmentInvitation apartmentInvitation = mapper.convert(invitation, ApartmentInvitation.class);
        if (!invitation.getEnabled().equals(true)) {
            throw new InvitationException(ALERT_INVITATION_NOT_ACTIVE_MESSAGE);
        }
        acceptUserInvitation(apartmentInvitation);
    }

    @Override
    public InvitationDto findInvitationByRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_REGISTRATION_TOKEN_MESSAGE));
        return mapper.convert(invitation, InvitationDto.class);
    }

    private boolean isApartmentInvitationNonExists(String email, Long id) {
        return apartmentInvitationRepository.findApartmentInvitationsByEmail(email).stream()
            .filter(invitation -> invitation.getStatus().equals(InvitationStatus.PROCESSING)
                || invitation.getStatus().equals(InvitationStatus.ACCEPTED))
            .filter(invitation -> invitation.getType().equals(InvitationType.APARTMENT))
            .filter(invitation -> invitation.getApartment().getId().equals(id)).findAny().isEmpty();
    }

    @Override
    public List<ApartmentInvitationDto> getAllUnsentLetters() {
        List<ApartmentInvitation> allNotSentInvitations = apartmentInvitationRepository
            .findAllBySentDatetimeIsNullAndEnabledEqualsAndStatusEqualsAndTypeEquals(true,
                InvitationStatus.PENDING, InvitationType.APARTMENT);
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, ApartmentInvitationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public void markInvitationsAsOverdue() {
        QApartmentInvitation qApartmentInvitation = QApartmentInvitation.apartmentInvitation;
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        List<ApartmentInvitation> overdueApartmentInvitations =
            query.select(qApartmentInvitation).from(qApartmentInvitation)
                .where((qApartmentInvitation.status.eq(InvitationStatus.PENDING))
                        .or(qApartmentInvitation.status.eq(InvitationStatus.PROCESSING)),
                    qApartmentInvitation.requestEndTime.before(LocalDateTime.now())).fetch();
        overdueApartmentInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.OVERDUE);
            apartmentInvitationRepository.save(invitation);
        });
    }

    @Override
    public void updateSentDateTimeAndStatus(Long id) {
        invitationService.updateSentDateTimeAndStatus(id);
    }

    @Override
    public void deactivateInvitation(Long id) {
    }

    @Override
    public Page<ApartmentInvitationDto> findAll(Integer pageNumber,
                                                Integer pageSize,
                                                Specification<ApartmentInvitation> specification) {
        Specification<ApartmentInvitation> invitationSpecification = specification
            .and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("enabled"), true));
        Page<ApartmentInvitation> pageCooperationInvitation = apartmentInvitationRepository
            .findAll(invitationSpecification, PageRequest.of(pageNumber - 1, pageSize));

        return pageCooperationInvitation.map(invitation -> mapper.convert(invitation, ApartmentInvitationDto.class));
    }
}
