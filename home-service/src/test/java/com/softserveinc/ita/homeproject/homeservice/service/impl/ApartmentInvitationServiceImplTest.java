package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ApartmentInvitationServiceImplTest {

    private static List<ApartmentInvitation> invitations;

    @Mock
    ApartmentInvitationRepository invitationRepository;

    @Mock
    ApartmentInvitationServiceImpl invitationService;


    @Test
    public void apartmentInvitations() {

        when(invitationRepository.findAll())
                .thenReturn(invitations);

        List<ApartmentInvitation> apartmentInvitations =
                (List<ApartmentInvitation>) invitationRepository.findAll();

        assertNotNull(invitationRepository);
        assertEquals(invitations, apartmentInvitations);
    }

    @Test
    public void testInvitationTime(){
        ApartmentInvitation invitation = invitations.get(0);
        assertTrue(invitation.getSentDatetime().compareTo(invitation.getRequestEndTime()) < 1);
    }

    @Test
    void testInvitationsDeactivating() {

        doNothing().when(invitationService).deactivateApartmentInvitations();

        invitationService.deactivateApartmentInvitations();
        invitations.forEach(i -> i.setStatus(InvitationStatus.DEACTIVATED));

        for (ApartmentInvitation invitation : invitations) {
            assertEquals(invitation.getStatus(), InvitationStatus.DEACTIVATED);
        }
    }


    @BeforeAll
    private static void addInvitations() {
        invitations = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            ApartmentInvitation invitation = new ApartmentInvitation();
            invitation.setId((long) i);
            invitation.setStatus(InvitationStatus.PROCESSING);
            invitation.setSentDatetime(LocalDateTime.now());
            invitation.setRequestEndTime(LocalDateTime.now().plusDays(7));
            invitations.add(invitation);
        }
    }
}
