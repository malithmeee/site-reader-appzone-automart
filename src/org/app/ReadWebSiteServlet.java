package org.app;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadWebSiteServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWebSiteServlet.class);
    private static String cronJob = "";
    Scheduler scheduler;

    public void init() throws ServletException {
        System.out.println("==================================================");
        propertyLoad();
        System.out.println("Initialized the cron job");
        try {
            System.out.println("Site reading cron job stated {" + cronJob + "}");
            SiteReadingCronJob(cronJob);
        } catch (Exception e) {
            LOGGER.error("Error occur while initialize the cron job", e);
        }
        System.out.println("==================================================");
    }

    public void SiteReadingCronJob(String cronExpression) throws Exception {
        JobDetail jobDetail = JobBuilder.newJob(DataReadingJob.class)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void destroy() {
        System.out.println("==================================================");
        System.out.println("Shutdown the cron job");
        try {
            System.out.println("Site reading cron job stopped {" + cronJob + "}");
            scheduler.shutdown();
        } catch (Exception e) {
            LOGGER.error("Error occur while shutdown the cron job", e);
        }
        System.out.println("==================================================");
    }

    private void propertyLoad() {
        Properties prop = new Properties();

        try {
            //load a properties file
//            prop.load(new FileInputStream("/home/malith/Projects/github/site-reader-appzone-automart/config.properties"));
            prop.load(new FileInputStream("/home/malithn/apache-tomcat-7.0.27/webapps/SiteReader/config.properties"));

            //get the property value and print it out
            Property.DATABASE = prop.getProperty("database");
            Property.DATABASE_USER = prop.getProperty("dbuser");
            Property.DATABASE_PW = prop.getProperty("dbpassword");
            cronJob = prop.getProperty("cron.job");

            System.out.println("Property file reading done...");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
