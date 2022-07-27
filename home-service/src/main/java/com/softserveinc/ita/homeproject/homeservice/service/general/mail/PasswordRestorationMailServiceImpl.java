package com.softserveinc.ita.homeproject.homeservice.service.general.mail;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.softserveinc.ita.homeproject.homeservice.dto.general.mail.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.poll.template.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    public static final String PASSWORD_RESET_EMAIL_SUBJECT = "Password Restoration Request";

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public LocalDateTime sendTextMessage(MailDto mailDto) throws MessagingException {
        log.debug("Message with type {} is being created", PASSWORD_RESET_EMAIL_SUBJECT);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(sender);
        helper.setTo(mailDto.getEmail());
        helper.setSubject(PASSWORD_RESET_EMAIL_SUBJECT);
        helper.setText(templateService.createMessageTextFromTemplate(mailDto), true);

        mailSender.send(message);
        log.info("Password restoration request with id {} was sent", mailDto.getId());
        return LocalDateTime.now();
    }
}
