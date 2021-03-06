package com.zhch.example.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author lifeix
 * Quartz tutorial address  http://www.quartz-scheduler.org/documentation/quartz-2.1.x/quick-start
 */
public class DefaultSchedulerExample {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultSchedulerExample.class);
	public static void startSchedule() {
		try {
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();
			// define the job and tie it to our HelloJob class
			JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();

			// job 触发器， 触发 job 执行， 每隔 1 秒执行一次
			Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow()
					.withSchedule(simpleSchedule().withIntervalInSeconds(1).repeatForever()).build();

			// 告诉 quartz 利用 trigger 来安排 job
			scheduler.scheduleJob(job, trigger);
			try {
				Thread.sleep(7000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			scheduler.shutdown();
		} catch (SchedulerException e) {
			LOG.error("Error occured when start shedule..", e);
		}
	}

	public static void main(String[] args) {
		//		Logger logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		startSchedule();
	}
}
