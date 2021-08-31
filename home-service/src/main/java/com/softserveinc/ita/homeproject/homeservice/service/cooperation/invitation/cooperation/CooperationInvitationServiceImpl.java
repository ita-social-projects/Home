package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.uuid.Generators;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.InvitationType;
import com.softserveinc.ita.homeproject.homedata.cooperation.repository.invitation.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.repository.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.user.cooperation.UserCooperationService;
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

        if(isCooperationInvitationNonExists(invitationDto.getEmail(), cooperationInvitation.getCooperationName())) {
            cooperationInvitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(7));
            cooperationInvitation.setCooperationName(cooperationInvitationDto.getCooperationName());
            cooperationInvitation.setStatus(InvitationStatus.PENDING);
            cooperationInvitation.setRegistrationToken(Generators.timeBasedGenerator().generate().toString());
            invitationRepository.save(cooperationInvitation);
            cooperationInvitationDto.setId(cooperationInvitation.getId());
            return cooperationInvitationDto;
        }
        throw new AlreadyExistHomeException("Invitation already exist for cooperation");
    }

    @Override
    public void acceptUserInvitation(Invitation invitation) {
        userCooperationService.createUserCooperationViaInvitation((CooperationInvitation) invitation);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    private boolean isCooperationInvitationNonExists(String email, String name){
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
}
