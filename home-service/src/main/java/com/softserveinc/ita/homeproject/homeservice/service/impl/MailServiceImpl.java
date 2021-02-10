package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import com.softserveinc.ita.homeproject.homeservice.service.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;
    private TemplateService templateService;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, TemplateServiceImpl templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    @Override
    public LocalDateTime sendTextMessage(InvitationDto invitationDto) throws MessagingException {
        log.debug("Message with invitation type {} is being created", invitationDto.getName());
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(invitationDto.getEmail());
        helper.setSubject(invitationDto.getName());
        helper.setText(templateService.createMessageTextFromTemplate(invitationDto), true);

        mailSender.send(message);
        log.info("Invitation with id {} was sent", invitationDto.getId());
        return LocalDateTime.now();
    }
}
