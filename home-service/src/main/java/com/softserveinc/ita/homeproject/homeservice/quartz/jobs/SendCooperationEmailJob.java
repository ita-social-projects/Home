package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class SendCooperationEmailJob {

    private final CooperationInvitationService cooperationInvitationService;

    private final ServiceMapper mapper;

    private final MailService mailService;

    private final UserRepository userRepository;

    @SneakyThrows
    public void executeAllInvitationsByType() {
        List<CooperationInvitationDto> invitations = cooperationInvitationService.getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            cooperationInvitationService.updateSentDateTimeAndStatus(invite.getId());
        }
    }

    private MailDto createMailDto(InvitationDto invitationDto) {
        CooperationInvitationDto invitation = mapper.convert(invitationDto, CooperationInvitationDto.class);
        var mailDto = new MailDto();
        mailDto.setType(invitation.getType());
        mailDto.setId(invitation.getId());
        mailDto.setEmail(invitation.getEmail());
        mailDto.setRole(invitation.getRole().getName());
        mailDto.setCooperationName(invitation.getCooperationName());
        checkRegistration(invitation, mailDto);
        return mailDto;
    }

    private void checkRegistration(CooperationInvitationDto invitationDto, MailDto mailDto) {
        if(userRepository.findByEmail(invitationDto.getEmail()).isEmpty()) {
            mailDto.setLink("https://home-project-academy.herokuapp.com/api/0/apidocs/index.html#post-/users");
            mailDto.setIsRegistered(false);
        }
        switch (invitationDto.getType()) {
            case COOPERATION:
                mailDto.setLink("Link for joining into cooperation");
                mailDto.setIsRegistered(true);
                break;
            case APARTMENT:
                mailDto.setLink("Link for joining into apartment");
                mailDto.setIsRegistered(true);
                break;
            default:
                throw new InvitationException("Wrong invitation type.");
        }
    }
}
