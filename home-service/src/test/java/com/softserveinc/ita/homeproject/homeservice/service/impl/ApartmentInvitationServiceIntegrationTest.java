package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.Apartment;
import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotAcceptableInvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment.ApartmentInvitationServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@SpringBootTest(classes = HomeServiceTestContextConfig.class)
public class ApartmentInvitationServiceIntegrationTest {

    @Autowired
    private ApartmentInvitationRepository apartmentInvitationRepository;

    @Autowired
    private InvitationServiceImpl invitationServiceImpl;

    @Autowired
    private ApartmentRepository apartmentRepository;

    private final String registrationToken = "e5dde7f8-627a-11ec-8240-bbcc8fff1c40";

    @BeforeEach
    public void initApartmentInvitationTestDB() {
        apartmentInvitationRepository.save(createNotOverduePendingApartmentInvitation());
        apartmentInvitationRepository.save(createOverduePendingApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueProcessingApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueProcessingApartmentInvitation());
        apartmentInvitationRepository.save(createNotOverdueAcceptedApartmentInvitation());
        apartmentInvitationRepository.save(createOverdueAcceptedApartmentInvitation());
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

        invitationServiceImpl.markInvitationsAsOverdue();

        List<ApartmentInvitation> allApartmentInvitationsFromTransformedDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(InvitationStatus.PENDING, allApartmentInvitationsFromBeginningDB.get(1).getStatus());
        assertEquals(InvitationStatus.PROCESSING, allApartmentInvitationsFromBeginningDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(3).getStatus());
    }

    @Test
    void deactivatedApartmentInvitationWhenInvitationStatusPendingAndEnableTrueTest() {
        Apartment apartment = createApartment();
        ApartmentInvitation invitation = createInvitation(apartment);

        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setEnabled(true);
        invitation.setSentDatetime(null);
        invitation = apartmentInvitationRepository.save(invitation);

        invitationServiceImpl.deactivateInvitation(invitation.getId());

        Optional<ApartmentInvitation> deactivatedInvitation =
            apartmentInvitationRepository.findById(invitation.getId());
        assertTrue(deactivatedInvitation.isPresent(), "expected that deactivated invitation found");
        assertEquals(false, deactivatedInvitation.get().getEnabled());
    }

    @ParameterizedTest
    @EnumSource(value = InvitationStatus.class,
        names = {"ACCEPTED", "PROCESSING", "OVERDUE"})
    void deactivatedApartmentInvitationWhenInvitationStatusNotPendingAndEnableTrueTest(InvitationStatus status) {
        Apartment apartment = createApartment();
        ApartmentInvitation invitation = createInvitation(apartment);

        invitation.setStatus(status);
        invitation.setEnabled(true);
        invitation.setSentDatetime(LocalDateTime.now());
        invitation = apartmentInvitationRepository.save(invitation);

        invitationServiceImpl.deactivateInvitation(invitation.getId());

        Optional <ApartmentInvitation> deactivetedInvitation =
            apartmentInvitationRepository.findById(invitation.getId());
        assertTrue(deactivetedInvitation.isPresent(), "expected that deactivated invitation found");
        assertEquals(false, deactivetedInvitation.get().getEnabled());

    }

    @Test
    void cannotDeactivateInvitationWhenStatusIsPendingAndEnableFalseTest() {
        Apartment apartment = createApartment();
        ApartmentInvitation invitation = createInvitation(apartment);

        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setSentDatetime(null);
        invitation.setEnabled(false);
        invitation = apartmentInvitationRepository.save(invitation);

        Long apartmentId = apartment.getId();
        Long invitationId = invitation.getId();

        NotFoundHomeException exception = Assertions.assertThrows(NotFoundHomeException.class, () ->
            invitationServiceImpl.deactivateInvitation(invitationId));
        assertEquals("Invitation with id: " + invitationId + " not found.", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value = InvitationStatus.class,
        names = {"ACCEPTED", "PROCESSING", "OVERDUE", "ERROR"})
    void cannotDeactivateInvitationWhenStatusIsNotPendingAndEnableFalseTest(InvitationStatus status) {
        Apartment apartment = createApartment();
        ApartmentInvitation invitation = createInvitation(apartment);

        invitation.setStatus(status);
        invitation.setEnabled(false);
        invitation.setSentDatetime(LocalDateTime.now());
        invitation = apartmentInvitationRepository.save(invitation);

        Long apartmentId = apartment.getId();
        Long invitationId = invitation.getId();

        NotFoundHomeException exception = Assertions.assertThrows(NotFoundHomeException.class, () ->
            invitationServiceImpl.deactivateInvitation(invitationId));
        assertEquals("Invitation with id: " + invitationId + " not found.", exception.getMessage());
    }

    @Test
    void cannotDeactivateInvitationWhenStatusIsNotExistTest() {
        Apartment apartment = createApartment();
        ApartmentInvitation invitation = createInvitation(apartment);

        invitation.setEnabled(true);

        invitation.setStatus(null);
        invitation = apartmentInvitationRepository.save(invitation);

        Long apartmentId = apartment.getId();
        Long invitationId = invitation.getId();

        Assertions.assertThrows(NullPointerException.class, () ->
            invitationServiceImpl.deactivateInvitation(invitationId));
    }

    @Test
    void cannotApproveOverdueInvitation() {
        ApartmentInvitation invitation = createOverdueApartmentInvitation();
        invitation.setRegistrationToken(registrationToken);
        invitation.setEnabled(true);
        apartmentInvitationRepository.save(invitation);
        NotAcceptableInvitationException exception = Assertions.assertThrows(NotAcceptableInvitationException.class, () ->
                invitationServiceImpl.registerWithRegistrationToken(invitation.getRegistrationToken()));
        assertEquals("Invitation was overdue", exception.getMessage());
    }

    @Test
    void cannotApproveDeletedInvitation() {
        Apartment apartment = createApartment();
        ApartmentInvitation invitation = createInvitation(apartment);
        invitation.setEnabled(false);
        invitation.setRegistrationToken(registrationToken);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        apartmentInvitationRepository.save(invitation);
        NotAcceptableInvitationException exception = Assertions.assertThrows(NotAcceptableInvitationException.class, () ->
                invitationServiceImpl.registerWithRegistrationToken(invitation.getRegistrationToken()));
        assertEquals("Invitation was deleted By Cooperation Admin", exception.getMessage());
    }

    private Apartment createApartment(){
        Apartment apartment = new Apartment();
        apartment.setApartmentNumber("123");
        apartment = apartmentRepository.save(apartment);
        return apartment;
    }

    private ApartmentInvitation createInvitation(Apartment apartment){
        ApartmentInvitation invitation = new ApartmentInvitation();
        invitation.setApartment(apartment);
        invitation = apartmentInvitationRepository.save(invitation);
        return invitation;
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
