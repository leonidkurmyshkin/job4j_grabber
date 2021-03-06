package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    private final Properties config;
    private final Connection cn;

    public AlertRabbit(Properties config, Connection cn) {
        this.config = config;
        this.cn = cn;
    }

    public static Connection initConnection(Properties config) throws Exception {
        Class.forName(config.getProperty("driver-class-name"));
        return DriverManager.getConnection(
                config.getProperty("url"),
                config.getProperty("username"),
                config.getProperty("password"));
    }

    public static Properties readConfig(String fileName) throws IOException {
        try (var in = AlertRabbit.class.getClassLoader().getResourceAsStream(fileName)) {
            Properties config = new Properties();
            config.load(in);
            return config;
        } catch (IOException e) {
            throw new IOException("Файл %s с настройками не найден.".formatted(fileName));
        }
    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            var store = (Connection) context.getJobDetail().getJobDataMap().get("store");
            var schema = (String) context.getJobDetail().getJobDataMap().get("schema");
            insertJobTime(store, schema);
        }

        private void insertJobTime(Connection store, String schema) {
            try (var ps = store.prepareStatement(
                    "INSERT INTO %s.rabbit(created_date) VALUES (?);"
                            .formatted(schema))) {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Scheduler schedulerStart() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDataMap store = new JobDataMap();
        store.put("store", cn);
        JobDetail job = newJob(Rabbit.class)
                .usingJobData(store)
                .usingJobData("schema", config.getProperty("rabbit.schema"))
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(config.getProperty("rabbit.interval")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
        scheduler.start();
        return scheduler;
    }

    public static void main(String[] args) {
        try {
            Properties config = readConfig("rabbit.properties");
            try (Connection cn = initConnection(config)) {
                var scheduler = new AlertRabbit(config, cn).schedulerStart();
                Thread.sleep(10000);
                scheduler.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}