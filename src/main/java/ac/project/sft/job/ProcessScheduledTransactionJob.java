package ac.project.sft.job;

import ac.project.sft.service.SchedulerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessScheduledTransactionJob {

    @Autowired
    SchedulerService service;


    @Scheduled(cron = "0 0/5 * * * ?")
    public void execute() throws JobExecutionException {
        service.executeScheduledTransactions();
    }



}
