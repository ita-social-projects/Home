package com.softserveinc.ita.homeproject.application.quartz.job;

import org.springframework.boot.autoconfigure.quartz.JobStoreType;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;

public class QuartzProp extends QuartzProperties {
    private JobStoreType jobStoreType = JobStoreType.JDBC;


}
