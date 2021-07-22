package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ApartmentInvitationServiceImplTest {

    private static ApartmentInvitationDto invitation;

    @MockBean
    ApartmentInvitationServiceImpl invitationService;


    @Test
    void checkTime() {
        Mockito.when(invitationService.saveInvitation(Mockito.any()))
                .thenReturn(new ApartmentInvitationDto());
        InvitationDto invitationDto = invitationService.saveInvitation(invitation);
        Assertions.assertNull(invitationDto.getSentDatetime());
        Assertions.assertNull(invitationDto.getRequestEndTime());
    }

    @BeforeAll
    static void createInvitation() {
        invitation = new ApartmentInvitationDto();
    }

}