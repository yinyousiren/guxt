package com.guxt.take.config;


import com.guxt.take.quartz.ClearImgJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail clearImgDetail(){
        return JobBuilder.newJob(ClearImgJob.class).storeDurably().build();
    }
    @Bean
    public Trigger clearImgTrigger(){
        ScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule("0 0/10 * * * ?");
        return TriggerBuilder.newTrigger().forJob(clearImgDetail()).withSchedule(schedBuilder).build();
    }
}
