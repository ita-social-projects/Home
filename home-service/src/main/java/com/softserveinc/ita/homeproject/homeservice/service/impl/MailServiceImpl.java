package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import com.softserveinc.ita.homeproject.homeservice.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService {
    private JavaMailSender mailSender;

    private TemplateService templateService;

    @Value("${spring.mail.username}")
    private String sender;

    private String headline = "Welcome to the club body!";

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, TemplateServiceImpl templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    @Override
    public LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException {
        log.debug("Message with invitation type {} is being created", headline);
        var message = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(mailDto.getEmail());
        helper.setSubject(headline);
        helper.setText(templateService.createMessageTextFromTemplate(mailDto), true);

        mailSender.send(message);
        log.info("Invitation with id {} was sent", mailDto.getId());
        return LocalDateTime.now();
    }
}
