package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import com.samskivert.mustache.Mustache;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
        try (Reader reader = new StringReader(Files.readString(getInvitationTemplate(mailDto)))) {
            text = Mustache.compiler()
                .compile(reader)
                .execute(mailDto);
        } catch (IOException e) {
            throw new InvitationException(Arrays.toString(e.getStackTrace()));
        }
        return text;
    }

    private Path getInvitationTemplate(MailDto mailDto) {
        if(!mailDto.getIsRegistered()) {
            return Path.of(registrationTemplatePath);
        }
        switch (mailDto.getType().toString()) {
            case "cooperation":
                return Path.of(cooperationTemplatePath);
            case "apartment":
                return Path.of(apartmentTemplatePath);
            default:
                throw new InvitationException("Wrong invitation type.");
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
