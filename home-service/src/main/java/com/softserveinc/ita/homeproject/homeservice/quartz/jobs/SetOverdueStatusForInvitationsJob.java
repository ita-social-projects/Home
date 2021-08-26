package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.impl.ApartmentInvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.impl.CooperationInvitationServiceImpl;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
@QuartzJobBeanAutoConfiguration(cron = "0 0 11,23 ? * *")
public class SetOverdueStatusForInvitationsJob extends QuartzJobBean {
    @Autowired
    private ApartmentInvitationServiceImpl apartmentInvitationService;

    @Autowired
    private CooperationInvitationServiceImpl cooperationInvitationService;

    @Override
    public void executeInternal(JobExecutionContext context) {
        apartmentInvitationService.markAsOverdueApartmentInvitations();
        cooperationInvitationService.markAsOverdueCooperationInvitations();
    }
}
