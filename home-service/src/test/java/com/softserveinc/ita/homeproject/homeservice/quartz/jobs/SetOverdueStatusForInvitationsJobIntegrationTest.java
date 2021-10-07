package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.HomeServiceTestContextConfig;
import com.softserveinc.ita.homeproject.homeservice.service.impl.ApartmentInvitationServiceIntegrationTest;
import com.softserveinc.ita.homeproject.homeservice.service.impl.CooperationInvitationServiceIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@SpringBootTest(classes = HomeServiceTestContextConfig.class)
public class SetOverdueStatusForInvitationsJobIntegrationTest {

    @Autowired
    private ApartmentInvitationRepository apartmentInvitationRepository;

    @Autowired
    private CooperationInvitationRepository cooperationInvitationRepository;

    @Autowired
    ApartmentInvitationServiceIntegrationTest apartmentInvitationServiceIntegrationTest;

    @Autowired
    CooperationInvitationServiceIntegrationTest cooperationInvitationServiceIntegrationTest;

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    @Qualifier("testSetOverdueStatusForInvitationsJob")
    JobDetailFactoryBean jobDetailFactoryBean;

    @Autowired
    SimpleTriggerFactoryBean triggerFactoryBean;

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
    void callSetOverdueStatusForInvitationsJobByQuartzBeansTest() throws SchedulerException, InterruptedException {
        Scheduler testScheduler = schedulerFactoryBean.getScheduler();
        testScheduler.scheduleJob(jobDetailFactoryBean.getObject(), triggerFactoryBean.getObject());

        testScheduler.start();
        Thread.sleep(1000 * 3);
        testScheduler.shutdown();

        List<CooperationInvitation> allCooperationInvitationsFromTransformedDB =
            StreamSupport.stream(cooperationInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        List<ApartmentInvitation> allApartmentInvitationsFromTransformedDB =
            StreamSupport.stream(apartmentInvitationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        assertEquals(InvitationStatus.OVERDUE, allCooperationInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allCooperationInvitationsFromTransformedDB.get(3).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(1).getStatus());
        assertEquals(InvitationStatus.OVERDUE, allApartmentInvitationsFromTransformedDB.get(3).getStatus());
    }
}
