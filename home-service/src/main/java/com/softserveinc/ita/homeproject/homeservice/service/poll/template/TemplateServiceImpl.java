package com.softserveinc.ita.homeproject.homeservice.service.poll.template;

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

    private static String registrationTemplatePath;

    private static String cooperationTemplatePath;

    private static String apartmentTemplatePath;

    @Override
    public String createMessageTextFromTemplate(MailDto mailDto) {
        var text = "";
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
            switch (mailDto.getType().toString()) {
                case "cooperation":
                    return Path.of(cooperationTemplatePath);
                case "apartment":
                    return Path.of(apartmentTemplatePath);
                default:
                    throw new InvitationException("Wrong invitation type.");
            }
        } else {
            return Path.of(registrationTemplatePath);
        }
    }

    @Value("${home.service.template.invitation.path.registration}")
    public void setRegistrationTemplatePath(String path) {
        TemplateServiceImpl.registrationTemplatePath = path;
    }

    @Value("${home.service.template.invitation.path.cooperation}")
    public void setCooperationTemplatePath(String path) {
        TemplateServiceImpl.cooperationTemplatePath = path;
    }

    @Value("${home.service.template.invitation.path.apartment}")
    public void setApartmentTemplatePath(String path) {
        TemplateServiceImpl.apartmentTemplatePath = path;
    }
}
