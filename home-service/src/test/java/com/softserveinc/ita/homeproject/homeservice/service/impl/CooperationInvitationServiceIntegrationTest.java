package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

@SpringBootTest(classes = HomeServiceTestContextConfig.class)
class CooperationInvitationServiceIntegrationTest {

    @Autowired
    private CooperationInvitationRepository cooperationInvitationRepository;

    @Autowired
    private CooperationInvitationServiceImpl cooperationInvitationService;

    @BeforeEach
    private void initCooperationInvitationsTestDB() {
        cooperationInvitationRepository.save(createNotOverduePendingInvitation());
        cooperationInvitationRepository.save(createOverduePendingInvitation());
        cooperationInvitationRepository.save(createNotOverdueProcessingInvitation());
        cooperationInvitationRepository.save(createOverdueProcessingInvitation());
        cooperationInvitationRepository.save(createNotOverdueAcceptedInvitation());
        cooperationInvitationRepository.save(createOverdueAcceptedInvitation());
        cooperationInvitationRepository.save(createNotOverdueDeclinedInvitation());
        cooperationInvitationRepository.save(createOverdueDeclinedInvitation());
        cooperationInvitationRepository.save(createNotOverdueDeactivatedInvitation());
        cooperationInvitationRepository.save(createOverdueDeactivatedInvitation());
        cooperationInvitationRepository.save(createOverdueInvitation());
        cooperationInvitationRepository.save(createNotOverdueErrorInvitation());
        cooperationInvitationRepository.save(createOverdueErrorInvitation());
    }

    @AfterEach
    private void dropCooperationInvitationsTestDB() {
        cooperationInvitationRepository.deleteAll();
    }

    @Test
    void getInvitationsForMarkingAsOverdueTest() {
        List<CooperationInvitation> allInvitationsFromBeginningDB =
            StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        List<CooperationInvitation> invitationsForOverdue = cooperationInvitationRepository
            .findAll((Specification<CooperationInvitation>) (root, criteriaQuery, criteriaBuilder) ->
                cooperationInvitationService.getOverdueInvitation(root, criteriaBuilder));

        assertEquals(13, allInvitationsFromBeginningDB.size());
        assertEquals(2, invitationsForOverdue.size());
        assertEquals(InvitationStatus.PENDING, invitationsForOverdue.get(0).getStatus());
        assertEquals(InvitationStatus.PROCESSING, invitationsForOverdue.get(1).getStatus());
    }

    @Test
    void markAsOverdueCooperationInvitationsTest() {
        List<CooperationInvitation> allInvitationsFromBeginningDB =
            StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        cooperationInvitationService.markAsOverdueCooperationInvitations();

        List<CooperationInvitation> allInvitationsFromTransformedDB =
            StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(InvitationStatus.PENDING, allInvitationsFromBeginningDB.get(1).getStatus());
        assertEquals(InvitationStatus.PROCESSING, allInvitationsFromBeginningDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allInvitationsFromTransformedDB.get(3).getStatus());
    }

    private CooperationInvitation createNotOverduePendingInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverduePendingInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueProcessingInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueProcessingInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueAcceptedInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueAcceptedInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueDeclinedInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueDeclinedInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueDeactivatedInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.DEACTIVATED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueDeactivatedInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.DEACTIVATED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.OVERDUE);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private CooperationInvitation createNotOverdueErrorInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private CooperationInvitation createOverdueErrorInvitation() {
        var invitation = new CooperationInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }
}
