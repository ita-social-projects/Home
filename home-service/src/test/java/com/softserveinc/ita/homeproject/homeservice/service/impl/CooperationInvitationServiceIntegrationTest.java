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
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation.CooperationInvitationServiceImpl;
import org.junit.jupiter.api.AfterEach;
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
    private CooperationInvitation createNotOverduePendingCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverduePendingCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueProcessingCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueProcessingCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueAcceptedCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueAcceptedCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.OVERDUE);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueErrorCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueErrorCooperationInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }
}
