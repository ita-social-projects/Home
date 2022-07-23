package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.general.mail.MailService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


public abstract class BaseEmailService {

    @Autowired
    protected CooperationRepository cooperationRepository;

    @Autowired
    protected ServiceMapper mapper;

    @Autowired
    protected MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Value("${home.project.ui.host}")
    private String uiHost;

    @SneakyThrows
    public void executeAllInvitationsByType() {
        List<? extends Mailable> letters = getMailableService().getAllUnsentLetters();

        for (Mailable letter : letters) {
            mailService.sendTextMessage(createMailDto(letter));
            getMailableService().updateSentDateTimeAndStatus(letter.getId());
        }
    }

    protected MailDto createMailDto(Mailable letter) {
        MailDto mailDto = createBaseMailDto(letter);
        mailDto.setId(letter.getId());
        mailDto.setEmail(letter.getEmail());
        mailDto.setRegistrationToken(letter.getToken());
        checkRegistration(letter, mailDto);
        return mailDto;
    }

    protected abstract MailDto createBaseMailDto(Mailable letter);

    protected void checkRegistration(Mailable letter, MailDto mailDto) {
        if (userRepository.findByEmail(letter.getEmail()).isEmpty()) {
            mailDto.setLink(getRegistrationUrl(mailDto));
            mailDto.setIsRegistered(false);
        } else {
            mailDto.setLink(
                uiHost + "/api/0/apidocs/index.html#post-/invitations/invitation-approval");
            mailDto.setIsRegistered(true);
        }
    }

    protected String getRegistrationUrl(MailDto mailDto) {
        return uiHost + String.format("/register-user?email=%s&token=%s", mailDto.getEmail(),
            mailDto.getRegistrationToken());
    }

    protected abstract MailableService getMailableService();
}
