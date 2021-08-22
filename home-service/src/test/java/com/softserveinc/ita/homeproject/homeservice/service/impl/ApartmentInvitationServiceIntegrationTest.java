package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

@SpringBootTest(classes = HomeServiceTestContextConfig.class)
class ApartmentInvitationServiceIntegrationTest {
    @Autowired
    private ApartmentInvitationRepository apartmentInvitationRepository;

    @Autowired
    private ApartmentInvitationServiceImpl apartmentInvitationService;

    @BeforeEach
    private void initApartmentInvitationsTestDB() {
        apartmentInvitationRepository.save(createNotOverduePendingInvitation());
        apartmentInvitationRepository.save(createOverduePendingInvitation());
        apartmentInvitationRepository.save(createNotOverdueProcessingInvitation());
        apartmentInvitationRepository.save(createOverdueProcessingInvitation());
        apartmentInvitationRepository.save(createNotOverdueAcceptedInvitation());
        apartmentInvitationRepository.save(createOverdueAcceptedInvitation());
        apartmentInvitationRepository.save(createNotOverdueDeclinedInvitation());
        apartmentInvitationRepository.save(createOverdueDeclinedInvitation());
        apartmentInvitationRepository.save(createNotOverdueDeactivatedInvitation());
        apartmentInvitationRepository.save(createOverdueDeactivatedInvitation());
        apartmentInvitationRepository.save(createOverdueInvitation());
        apartmentInvitationRepository.save(createNotOverdueErrorInvitation());
        apartmentInvitationRepository.save(createOverdueErrorInvitation());
    }

    @AfterEach
    private void dropApartmentInvitationsTestDB() {
        apartmentInvitationRepository.deleteAll();
    }

    @Test
    void getInvitationsForMarkingAsOverdueTest() {
        List<ApartmentInvitation> allInvitationsFromBeginningDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        List<ApartmentInvitation> invitationsForOverdue = apartmentInvitationRepository
            .findAll((Specification<ApartmentInvitation>) (root, criteriaQuery, criteriaBuilder) ->
                apartmentInvitationService.getOverdueInvitation(root, criteriaBuilder));

        assertEquals(13, allInvitationsFromBeginningDB.size());
        assertEquals(2, invitationsForOverdue.size());
        assertEquals(InvitationStatus.PENDING, invitationsForOverdue.get(0).getStatus());
        assertEquals(InvitationStatus.PROCESSING, invitationsForOverdue.get(1).getStatus());
    }

    @Test
    void markAsOverdueApartmentInvitationsTest() {
        List<ApartmentInvitation> allInvitationsFromBeginningDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        apartmentInvitationService.markAsOverdueApartmentInvitations();

        List<ApartmentInvitation> allInvitationsFromTransformedDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(InvitationStatus.PENDING, allInvitationsFromBeginningDB.get(1).getStatus());
        assertEquals(InvitationStatus.PROCESSING, allInvitationsFromBeginningDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allInvitationsFromTransformedDB.get(3).getStatus());
    }

    private ApartmentInvitation createNotOverduePendingInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverduePendingInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueProcessingInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueProcessingInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueAcceptedInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueAcceptedInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueDeclinedInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueDeclinedInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueDeactivatedInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DEACTIVATED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueDeactivatedInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DEACTIVATED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.OVERDUE);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueErrorInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueErrorInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }
}
