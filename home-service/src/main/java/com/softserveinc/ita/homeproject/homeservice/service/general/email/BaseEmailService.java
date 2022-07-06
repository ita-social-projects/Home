package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.general.mail.MailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseEmailService<T extends Invitation, D extends InvitationDto> {

    @Autowired
    protected CooperationRepository cooperationRepository;

    @Autowired
    protected ServiceMapper mapper;

    @Autowired
    protected MailService mailService;

    @Autowired
    private UserRepository userRepository;

    private static final String UI_HOST = "https://home-project-ui.herokuapp.com";


    @SneakyThrows
    public void executeAllInvitationsByType() {
        List<D> invitations = getInvitationService().getAllActiveInvitations();

        for (InvitationDto invite : invitations) {
            mailService.sendTextMessage(createMailDto(invite));
            getInvitationService().updateSentDateTimeAndStatus(invite.getId());
        }
    }

    protected MailDto createMailDto(InvitationDto invitationDto) {
        MailDto mailDto = createBaseMailDto(invitationDto);
        mailDto.setId(invitationDto.getId());
        mailDto.setEmail(invitationDto.getEmail());
        mailDto.setRegistrationToken(invitationDto.getRegistrationToken());
        checkRegistration(invitationDto, mailDto);
        return mailDto;
    }

    protected abstract MailDto createBaseMailDto(InvitationDto invitationDto);

    protected void checkRegistration(InvitationDto invitationDto, MailDto mailDto) {
        if (userRepository.findByEmail(invitationDto.getEmail()).isEmpty()) {
            mailDto.setLink(getRegistrationUrl(mailDto));
            mailDto.setIsRegistered(false);
        } else {
            mailDto.setLink(
                "https://home-project-academy.herokuapp.com/api/0/apidocs/index.html#post-/invitations/invitation-approval");
            mailDto.setIsRegistered(true);
        }
    }

    protected String getRegistrationUrl(MailDto mailDto) {
        return UI_HOST + String.format("/register-user?email=%s&token=%s", mailDto.getEmail(),
            mailDto.getRegistrationToken());
    }

    protected abstract InvitationService<T, D> getInvitationService();
}
