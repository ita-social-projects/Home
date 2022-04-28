package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableInvitationException;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation.CooperationInvitationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@SpringBootTest(classes = HomeServiceTestContextConfig.class)
public class CooperationInvitationServiceIntegrationTest {

    @Autowired
    private CooperationInvitationRepository cooperationInvitationRepository;

    @Autowired
    private CooperationInvitationServiceImpl cooperationInvitationService;

    @Autowired
    private InvitationServiceImpl invitationServiceImpl;

    private final String registrationToken = "4d45db2a-6970-11ec-a4bd-e75cc40d7df1";

    @BeforeEach
    public void initCooperationInvitationTestDB() {
        cooperationInvitationRepository.save(createNotOverduePendingCooperationInvitation());
        cooperationInvitationRepository.save(createOverduePendingCooperationInvitation());
        cooperationInvitationRepository.save(createNotOverdueProcessingCooperationInvitation());
        cooperationInvitationRepository.save(createOverdueProcessingCooperationInvitation());
        cooperationInvitationRepository.save(createNotOverdueAcceptedCooperationInvitation());
        cooperationInvitationRepository.save(createOverdueAcceptedCooperationInvitation());
        cooperationInvitationRepository.save(createOverdueCooperationInvitation());
        cooperationInvitationRepository.save(createNotOverdueErrorCooperationInvitation());
        cooperationInvitationRepository.save(createOverdueErrorCooperationInvitation());
    }

    @AfterEach
    public void dropCooperationInvitationTestDB() {
        cooperationInvitationRepository.deleteAll();
    }

    @Test
    void markAsOverdueCooperationInvitationsTest() {
        List<CooperationInvitation> allCooperationInvitationsFromBeginningDB =
                StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());

        cooperationInvitationService.markInvitationsAsOverdue();

        List<CooperationInvitation> allCooperationInvitationsFromTransformedDB =
                StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                        .collect(Collectors.toList());

        assertEquals(InvitationStatus.PENDING, allCooperationInvitationsFromBeginningDB.get(1).getStatus());
        assertEquals(InvitationStatus.PROCESSING, allCooperationInvitationsFromBeginningDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allCooperationInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allCooperationInvitationsFromTransformedDB.get(3).getStatus());
    }

    @Test
    void cannotApproveOverdueCooperationInvitation() {
        CooperationInvitation invitation = createOverdueCooperationInvitation();
        invitation.setRegistrationToken(registrationToken);
        invitation.setEnabled(true);
        cooperationInvitationRepository.save(invitation);
        NotAcceptableInvitationException exception = Assertions.assertThrows(NotAcceptableInvitationException.class,
                () -> invitationServiceImpl.registerWithRegistrationToken(invitation.getRegistrationToken()));
        assertEquals("Invitation was overdue", exception.getMessage());
    }

    @Test
    void cannotApproveDeletedICooperationInvitation() {
        CooperationInvitation invitation = createNotOverdueAcceptedCooperationInvitation();
        invitation.setEnabled(false);
        invitation.setRegistrationToken(registrationToken);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        cooperationInvitationRepository.save(invitation);
        NotAcceptableInvitationException exception = Assertions.assertThrows(NotAcceptableInvitationException.class,
                () -> invitationServiceImpl.registerWithRegistrationToken(invitation.getRegistrationToken()));
        assertEquals("Invitation was deleted By Cooperation Admin", exception.getMessage());
    }

    private CooperationInvitation createNotOverduePendingCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverduePendingCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueProcessingCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueProcessingCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueAcceptedCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueAcceptedCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.OVERDUE);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueErrorCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueErrorCooperationInvitation() {
        CooperationInvitation invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }
}
