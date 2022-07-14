package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import java.util.List;


public interface MailableService {
    List<? extends Mailable> getAllUnsentLetters();

    void updateSentDateTimeAndStatus(Long id);
}
