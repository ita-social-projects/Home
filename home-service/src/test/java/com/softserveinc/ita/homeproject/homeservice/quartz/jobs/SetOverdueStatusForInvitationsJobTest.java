package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.homeservice.service.impl.ApartmentInvitationServiceIntegrationTest;
import com.softserveinc.ita.homeproject.homeservice.service.impl.CooperationInvitationServiceIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HomeServiceTestContextConfig.class)
public class SetOverdueStatusForInvitationsJobTest {

    @Autowired
    private ApartmentInvitationRepository apartmentInvitationRepository;

    @Autowired
    private CooperationInvitationRepository cooperationInvitationRepository;

    @Autowired
    ApartmentInvitationServiceIntegrationTest apartmentInvitationServiceIntegrationTest;

    @Autowired
    CooperationInvitationServiceIntegrationTest cooperationInvitationServiceIntegrationTest;

    @Autowired
    private SetOverdueStatusForInvitationsJob setOverdueStatusForInvitationsJob;

    @BeforeEach
    private void initInvitationTestDB() {
        apartmentInvitationServiceIntegrationTest.initApartmentInvitationTestDB();
        cooperationInvitationServiceIntegrationTest.initCooperationInvitationTestDB();
    }

    @AfterEach
    private void dropInvitationTestDB() {
        apartmentInvitationServiceIntegrationTest.dropApartmentInvitationTestDB();
        cooperationInvitationServiceIntegrationTest.dropCooperationInvitationTestDB();
    }

    @Test
    void setOverdueStatusForInvitationsJobTest() {
        setOverdueStatusForInvitationsJob.executeInternal(null);

        List<CooperationInvitation> allCooperationInvitationsFromTransformedDB =
            StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        allCooperationInvitationsFromTransformedDB.forEach(System.out::println);

        List<ApartmentInvitation> allApartmentInvitationsFromTransformedDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        allApartmentInvitationsFromTransformedDB.forEach(System.out::println);

        assertEquals(InvitationStatus.OVERDUE, allCooperationInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allCooperationInvitationsFromTransformedDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(3).getStatus());
    }
}
