package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .ALERT_INVITATION_ALREADY_EXIST_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .ALERT_INVITATION_NOT_ACTIVE_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_FOUND_REGISTRATION_TOKEN_MESSAGE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasterxml.uuid.Generators;
import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.QCooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleEnum;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CooperationInvitationServiceImpl
    implements InvitationService<CooperationInvitation, CooperationInvitationDto> {


    @PersistenceContext
    protected EntityManager entityManager;

    private final ServiceMapper mapper;

    private static final int EXPIRATION_TERM = 7;

    private final CooperationInvitationRepository cooperationInvitationRepository;

    private final InvitationService<Invitation, InvitationDto> invitationService;

    private final InvitationRepository invitationRepository;

    private final UserCooperationService userCooperationService;

    public CooperationInvitationServiceImpl(ServiceMapper mapper,
                                            CooperationInvitationRepository cooperationInvitationRepository,
                                            EntityManager entityManager,
                                            InvitationService<Invitation, InvitationDto> invitationService,
                                            InvitationRepository invitationRepository,
                                            UserCooperationService userCooperationService) {

        this.cooperationInvitationRepository = cooperationInvitationRepository;
        this.invitationService = invitationService;
        this.invitationRepository = invitationRepository;
        this.entityManager = entityManager;
        this.mapper = mapper;
        this.userCooperationService = userCooperationService;
    }

    @Override
    public CooperationInvitationDto createInvitation(CooperationInvitationDto cooperationInvitationDto) {
        CooperationInvitation cooperationInvitation =
            mapper.convert(cooperationInvitationDto, CooperationInvitation.class);

        if (isCooperationInvitationNonExists(cooperationInvitationDto.getEmail(),
            cooperationInvitation.getCooperationId())) {
            cooperationInvitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(EXPIRATION_TERM));
            cooperationInvitation.setCooperationId(cooperationInvitationDto.getCooperationId());
            cooperationInvitation.setStatus(InvitationStatus.PENDING);
            cooperationInvitation.setRole(RoleEnum.COOPERATION_ADMIN);
            cooperationInvitation.setRegistrationToken(Generators.timeBasedGenerator().generate().toString());
            cooperationInvitation.setEnabled(true);
            cooperationInvitationRepository.save(cooperationInvitation);
            cooperationInvitationDto.setId(cooperationInvitation.getId());
            return cooperationInvitationDto;
        }
        throw new IllegalStateException(ALERT_INVITATION_ALREADY_EXIST_MESSAGE);
    }

    @Override
    public void acceptUserInvitation(CooperationInvitation cooperationInvitation) {
        userCooperationService.createUserCooperationViaInvitation(cooperationInvitation);
        cooperationInvitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(cooperationInvitation);
    }

    @Override
    public void registerWithRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_REGISTRATION_TOKEN_MESSAGE));
        CooperationInvitation cooperationInvitation = mapper.convert(invitation, CooperationInvitation.class);
        if (!invitation.getEnabled().equals(true)) {
            throw new InvitationException(ALERT_INVITATION_NOT_ACTIVE_MESSAGE);
        }
        acceptUserInvitation(cooperationInvitation);
    }


    @Override
    public InvitationDto findInvitationByRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_REGISTRATION_TOKEN_MESSAGE));
        return mapper.convert(invitation, InvitationDto.class);
    }

    private boolean isCooperationInvitationNonExists(String email, Long cooperationId) {
        return cooperationInvitationRepository.findCooperationInvitationsByEmail(email).stream()
            .filter(invitation -> invitation.getStatus().equals(InvitationStatus.PROCESSING)
                || invitation.getStatus().equals(InvitationStatus.ACCEPTED))
            .filter(invitation -> invitation.getType().equals(InvitationType.COOPERATION))
            .filter(invitation -> invitation.getCooperationId().equals(cooperationId)).findAny().isEmpty();
    }

    @Override
    public void markInvitationsAsOverdue() {
        QCooperationInvitation qCooperationInvitation = QCooperationInvitation.cooperationInvitation;
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        List<CooperationInvitation> overdueCooperationInvitations =
            query.select(qCooperationInvitation).from(qCooperationInvitation)
                .where((qCooperationInvitation.status.eq(InvitationStatus.PENDING))
                        .or(qCooperationInvitation.status.eq(InvitationStatus.PROCESSING)),
                    qCooperationInvitation.requestEndTime.before(LocalDateTime.now())).fetch();
        overdueCooperationInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.OVERDUE);
            cooperationInvitationRepository.save(invitation);
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
    public List<CooperationInvitationDto> getAllUnsentLetters() {
        List<CooperationInvitation> allNotSentInvitations = cooperationInvitationRepository
            .findAllBySentDatetimeIsNullAndStatusEqualsAndEnabledEqualsAndTypeEquals(
                InvitationStatus.PENDING, true, InvitationType.COOPERATION);
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public Page<CooperationInvitationDto> findAll(Integer pageNumber,
                                                  Integer pageSize,
                                                  Specification<CooperationInvitation> specification) {
        Specification<CooperationInvitation> invitationSpecification = specification
            .and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("enabled"), true));
        Page<CooperationInvitation> pageCooperationInvitation = cooperationInvitationRepository
            .findAll(invitationSpecification, PageRequest.of(pageNumber - 1, pageSize));

        return pageCooperationInvitation.map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class));
    }
}
