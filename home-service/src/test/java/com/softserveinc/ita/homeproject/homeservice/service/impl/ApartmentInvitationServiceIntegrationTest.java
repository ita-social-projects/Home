package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment.ApartmentInvitationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@SpringBootTest(classes = HomeServiceTestContextConfig.class)
public class ApartmentInvitationServiceIntegrationTest {

    @Autowired
    private ApartmentInvitationRepository apartmentInvitationRepository;

    @Autowired
    private ApartmentInvitationServiceImpl apartmentInvitationService;

    @BeforeEach
    public void initApartmentInvitationTestDB() {
        apartmentInvitationRepository.save(createNotOverduePendingApartmentInvitation());
        apartmentInvitationRepository.save(createOverduePendingApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueProcessingApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueProcessingApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueAcceptedApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueAcceptedApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueDeclinedApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueDeclinedApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueDeactivatedApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueDeactivatedApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueErrorApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueErrorApartmentInvitation());
    }

    @AfterEach
    public void dropApartmentInvitationTestDB() {
        apartmentInvitationRepository.deleteAll();
    }

    @Test
    void markAsOverdueApartmentInvitationsTest() {
        List<ApartmentInvitation> allApartmentInvitationsFromBeginningDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        apartmentInvitationService.markInvitationsAsOverdue();

        List<ApartmentInvitation> allApartmentInvitationsFromTransformedDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(InvitationStatus.PENDING, allApartmentInvitationsFromBeginningDB.get(1).getStatus());
        assertEquals(InvitationStatus.PROCESSING, allApartmentInvitationsFromBeginningDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(3).getStatus());
    }

    private ApartmentInvitation createNotOverduePendingApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverduePendingApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueProcessingApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueProcessingApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueAcceptedApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueAcceptedApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueDeclinedApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueDeclinedApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DECLINED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueDeactivatedApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DEACTIVATED);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueDeactivatedApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.DEACTIVATED);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.OVERDUE);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }

    private ApartmentInvitation createNotOverdueErrorApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().plusDays(1));
        return invitation;
    }

    private ApartmentInvitation createOverdueErrorApartmentInvitation() {
        var invitation = new ApartmentInvitation();
        invitation.setStatus(InvitationStatus.ERROR);
        invitation.setRequestEndTime(LocalDateTime.now().minusDays(1));
        return invitation;
    }
}
