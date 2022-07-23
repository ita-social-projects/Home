package com.softserveinc.ita.homeproject.homeservice.service.general.mail;

import java.time.LocalDateTime;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.poll.template.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRestorationMailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final TemplateService templateService;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException {
        String headline = "Password Restoration Request";
        log.debug("Message with type {} is being created", headline);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(mailDto.getEmail());
        helper.setSubject(headline);
        helper.setText(templateService.createMessageTextFromTemplate(mailDto), true);

        mailSender.send(message);
        log.info("Password restoration request with id {} was sent", mailDto.getId());
        return LocalDateTime.now();
    }
}
