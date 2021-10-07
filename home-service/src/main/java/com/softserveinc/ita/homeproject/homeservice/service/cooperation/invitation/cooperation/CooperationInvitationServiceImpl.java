package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.uuid.Generators;
import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.QCooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import org.springframework.stereotype.Service;

@Service
public class CooperationInvitationServiceImpl extends InvitationServiceImpl implements CooperationInvitationService {

    private final CooperationInvitationRepository cooperationInvitationRepository;

    private final UserCooperationService userCooperationService;

    public CooperationInvitationServiceImpl(InvitationRepository invitationRepository,
                                            ServiceMapper mapper,
                                            CooperationInvitationRepository cooperationInvitationRepository,
                                            UserCooperationService userCooperationService) {
        super(invitationRepository, mapper);
        this.cooperationInvitationRepository = cooperationInvitationRepository;
        this.userCooperationService = userCooperationService;
    }

    @Override
    protected InvitationDto saveInvitation(InvitationDto invitationDto) {
        var cooperationInvitationDto =
            mapper.convert(invitationDto, CooperationInvitationDto.class);
        var cooperationInvitation =
            mapper.convert(cooperationInvitationDto, CooperationInvitation.class);

        if (isCooperationInvitationNonExists(invitationDto.getEmail(), cooperationInvitation.getCooperationName())) {
            cooperationInvitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(EXPIRATION_TERM));
            cooperationInvitation.setCooperationName(cooperationInvitationDto.getCooperationName());
            cooperationInvitation.setStatus(InvitationStatus.PENDING);
            cooperationInvitation.setRegistrationToken(Generators.timeBasedGenerator().generate().toString());
            invitationRepository.save(cooperationInvitation);
            cooperationInvitationDto.setId(cooperationInvitation.getId());
            return cooperationInvitationDto;
        }
        throw new BadRequestHomeException("Invitation already exist for cooperation");
    }

    @Override
    public void acceptUserInvitation(Invitation invitation) {
        userCooperationService.createUserCooperationViaInvitation((CooperationInvitation) invitation);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    private boolean isCooperationInvitationNonExists(String email, String name) {
        return cooperationInvitationRepository.findCooperationInvitationsByEmail(email).stream()
            .filter(invitation -> invitation.getStatus().equals(InvitationStatus.PROCESSING)
                || invitation.getStatus().equals(InvitationStatus.ACCEPTED))
            .filter(invitation -> invitation.getType().equals(InvitationType.COOPERATION))
            .filter(invitation -> invitation.getCooperationName().equals(name)).findAny().isEmpty();
    }

    @Override
    public List<CooperationInvitationDto> getAllActiveInvitations() {
        var allNotSentInvitations = cooperationInvitationRepository
            .findAllBySentDatetimeIsNullAndStatusEquals(
                InvitationStatus.PENDING);
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public void markInvitationsAsOverdue() {
        var qCooperationInvitation = QCooperationInvitation.cooperationInvitation;
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        var overdueCooperationInvitations = query.select(qCooperationInvitation).from(qCooperationInvitation)
            .where((qCooperationInvitation.status.eq(InvitationStatus.PENDING))
                    .or(qCooperationInvitation.status.eq(InvitationStatus.PROCESSING)),
                qCooperationInvitation.requestEndTime.before(LocalDateTime.now())).fetch();
        overdueCooperationInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.OVERDUE);
            cooperationInvitationRepository.save(invitation);
        });
    }
}
