package ru.job4j.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.Config;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final static String START_LINK = "https://www.sql.ru/forum/job-offers/%s";
    private final static int NUM_OF_PAGES = 5;
    private final Properties cfg;

    public Grabber(Properties cfg) {
        this.cfg = cfg;
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            for (var i = 1; i <= NUM_OF_PAGES; i++) {
                parse.list(START_LINK.formatted(i))
                        .forEach(store::save);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Properties cfg = Config.read("grabber.properties");
        Grabber grab = new Grabber(cfg);
        grab.init(new SqlRuParse(new SqlRuDateTimeParser()),
                new PsqlStore(cfg),
                grab.scheduler());
    }
}