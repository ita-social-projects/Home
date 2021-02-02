package com.softserveinc.ita.homeproject.homeservice.service;

public interface MailService {

    void sendTextMessage(String receiver, String subject, String text);

    void sendMessageWithAttachment(String receiver, String subject, String text);
}
