package com.ContestSite.CodeCompiler.Scheduler;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Executor.JobExecutor;
import lombok.Builder;

import java.util.concurrent.*;

public class JobScheduler {

    private static BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    /**
     * Starts threads which picks up Jobs from Queues
     */
    public static void initialize() {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(8, 8, 1,
                TimeUnit.MINUTES, taskQueue);
        executorService.prestartAllCoreThreads();

        new Thread(() -> {
            while(true) {
                if(taskQueue.size() <= 20) {
                    taskQueue.add(Task.builder().jobId(pickJob()).build());
                }
            }
        }).start();
    }

    /**
     * Pops a User from UserQueue using UserQueueHandler
     * Picks a Job for this User from JobQueues using JobQueuesHandler
     * Pushes the User into the UserQueue using UserQueueHandler
     */
    public synchronized static String pickJob() {
        String username = UserQueueHandler.pop();
        String jobId = JobQueuesHandler.pickUserOldestJob(username);
        UserQueueHandler.push(username);
        return jobId;
    }

    @Builder
    static class Task implements Runnable {

        private String jobId;

        @Override
        public void run() {
            Job job = JobContextHandler.getJobContext(jobId);
            try {
                JobExecutor jobExecutor = JobExecutor.getJobExecutor(job).newInstance();
                jobExecutor.execute(job);
            } catch (Exception e) {
                throw new RuntimeException("Job could not be executed : " + jobId);
            }
        }
    }

}
