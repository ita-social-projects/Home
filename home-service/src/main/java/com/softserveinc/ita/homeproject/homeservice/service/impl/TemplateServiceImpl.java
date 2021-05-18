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
import org.springframework.stereotype.Service;

@Service
public class TemplateServiceImpl implements TemplateService {
    public static final Path REGISTRATION_TEMPLATE_PATH =
        Path.of("home-data/src/main/resources/template/invitation-to-registration.mustache");

    public static final Path COOPERATION_TEMPLATE_PATH =
        Path.of("home-data/src/main/resources/template/invitation-to-cooperation.mustache");

    private final String headline = "invitation-to-registration";

    @Override
    public String createMessageTextFromTemplate(MailDto mailDto) {
        String text = "";
        try (Reader reader = new StringReader(Files.readString(getInvitationTemplate(headline)))) {
            text = Mustache.compiler()
                .compile(reader)
                .execute(mailDto);
        } catch (IOException e) {
            throw new InvitationException(Arrays.toString(e.getStackTrace()));
        }
        return text;
    }

    private Path getInvitationTemplate(String invitationName) {
        Path path;
        switch (invitationName) {
            case "invitation-to-registration":
                path = REGISTRATION_TEMPLATE_PATH;
                break;
            case "invitation-to-cooperation":
                path = COOPERATION_TEMPLATE_PATH;
                break;
            default:
                throw new InvitationException("Wrong invitation name");
        }
        return path;
    }
}
