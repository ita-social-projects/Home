package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.samskivert.mustache.Mustache;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Override
    public String createMessageTextFromTemplate(InvitationDto invitationDto) {
        String text = "";
        try (Reader reader = new StringReader(Files.readString(getInvitationTemplate(invitationDto.getName())))) {
            text = Mustache.compiler()
                .compile(reader)
                .execute(invitationDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    private Path getInvitationTemplate(String invitationName) {
        Path path;
        switch (invitationName) {
        case "invitation-to-registration":
            path = Path.of("home-data/src/main/resources/template/invitation-to-registration.mustache");
            break;
        case "invitation-to-cooperation":
            path = Path.of("home-data/src/main/resources/template/invitation-to-cooperation.mustache");
            break;
        default:
            throw new InvitationException("Wrong invitation name");
        }
        return path;
    }
}
