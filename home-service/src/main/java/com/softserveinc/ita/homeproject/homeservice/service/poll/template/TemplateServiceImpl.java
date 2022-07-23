package com.softserveinc.ita.homeproject.homeservice.service.poll.template;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_WRONG_INVITATION_TYPE_MESSAGE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Arrays;

import com.samskivert.mustache.Mustache;
import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:/home-service.properties")
public class TemplateServiceImpl implements TemplateService {

    @Value("${home.service.template.invitation.path.registration}")
    private String registrationTemplatePath;

    @Value("${home.service.template.invitation.path.cooperation}")
    private String cooperationTemplatePath;

    @Value("${home.service.template.invitation.path.apartment}")
    private String apartmentTemplatePath;

    @Value("${home.service.template.password.restoration.path}")
    private static String passwordRestorationTemplatePath;

    @Override
    public String createMessageTextFromTemplate(MailDto mailDto) {
        String text = "";
        try (InputStream resource = new ClassPathResource(
                getInvitationTemplate(mailDto).toString()).getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(resource))) {
            text = Mustache.compiler()
                    .compile(reader)
                    .execute(mailDto);
        } catch (IOException e) {
            throw new InvitationException(Arrays.toString(e.getStackTrace()));
        }
        return text;
    }

    private Path getInvitationTemplate(MailDto mailDto) {
        if (mailDto.getIsRegistered()) {
            switch (mailDto.getType()) {
                case "cooperation":
                    return Path.of(cooperationTemplatePath);
                case "apartment":
                    return Path.of(apartmentTemplatePath);
                case "passwordRestoration" :
                    return Path.of(passwordRestorationTemplatePath);
                default:
                    throw new InvitationException(ALERT_WRONG_INVITATION_TYPE_MESSAGE);
            }
        } else {
            return Path.of(registrationTemplatePath);
        }
    }
}
