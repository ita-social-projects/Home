package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.general.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class SendPasswordRestorationApprovalEmailServiceImpl extends BaseEmailService {

    private final MailableService mailableService;

    @Autowired
    public SendPasswordRestorationApprovalEmailServiceImpl(
        @Qualifier("passwordRestorationServiceImpl") MailableService mailableService,
        @Qualifier(value = "passwordRestorationMailServiceImpl") MailService mailService) {
        super(mailService);
        this.mailableService = mailableService;
    }

    @Override
    protected MailDto createBaseMailDto(Mailable letter) {
        MailDto mailDto = new MailDto();
        mailDto.setType("passwordRestoration");
        mailDto.setToken(letter.getToken());

        return mailDto;
    }

    @Override
    protected void generateLink(Mailable letter, MailDto mailDto) {
        User user = userRepository.findByEmail(letter.getEmail())
            .orElseThrow(() -> new NotFoundHomeException(ExceptionMessages.NOT_FOUND_USER_WITH_CURRENT_EMAIL_MESSAGE));

        mailDto.setLink(swaggerHost
            + "/api/0/apidocs/swagger-ui/index.html#/reset%20password/passwordRestorationApproval?"
            + "email=" + user.getEmail() + "&token=" + letter.getToken());
        mailDto.setIsRegistered(true);
    }

    @Override
    protected MailableService getMailableService() {
        return mailableService;
    }
}
