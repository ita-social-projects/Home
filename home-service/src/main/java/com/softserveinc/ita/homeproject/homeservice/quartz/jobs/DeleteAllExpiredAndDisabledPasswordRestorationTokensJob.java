package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import com.softserveinc.ita.homeproject.homeservice.quartz.config.QuartzJobBeanAutoConfiguration;
import com.softserveinc.ita.homeproject.homeservice.service.user.password.PasswordRestorationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
@QuartzJobBeanAutoConfiguration(cron = "${home.jobs.erase-password-restoration-tokens.cron}")
public class DeleteAllExpiredAndDisabledPasswordRestorationTokensJob extends QuartzJobBean {

    private final PasswordRestorationServiceImpl passwordRestorationService;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("Start password restoration token erasure");
        passwordRestorationService.deleteAllExpiredAndDisabledPasswordRestorationTokens();
    }
}
