package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.service.TemplateService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(SpringExtension.class)
class MailServiceImplTest {
    private static MailDto mailDto;

    @InjectMocks
    private MailServiceImpl mailService;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateService templateService;

    @BeforeAll
    static void getConstants() {
        mailDto = new MailDto();
        mailDto.setType(InvitationTypeDto.COOPERATION);
        mailDto.setId(111L);
        mailDto.setEmail("G.Y.Andreevich@gmail.com");
        mailDto.setRole("admin");
        mailDto.setCooperationName("Main cooperation");
        mailDto.setIsRegistered(true);
        mailDto.setLink("some-link");
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(mailService, "sender", "test.ita.emails@gmail.com");
    }

    @Test
    void sendTextMessageTest() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateService.createMessageTextFromTemplate(mailDto)).thenReturn("Welcome to the club body!");

        mailService.sendTextMessage(mailDto);
        assertEquals("Welcome to the club body!", templateService.createMessageTextFromTemplate(mailDto));
    }
}
