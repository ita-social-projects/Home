package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.util.Set;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationServiceImpl;
import lombok.NoArgsConstructor;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@QuartzJobBeanAutoConfiguration(cron = "0 0 11,23 ? * *")
public class SetOverdueStatusForInvitationsJob extends QuartzJobBean {
    @Autowired
    private Set<InvitationServiceImpl> invitationServices;

    @Override
    public void executeInternal(JobExecutionContext context) {
        invitationServices.forEach(InvitationServiceImpl::markInvitationsAsOverdue);
    }
}
