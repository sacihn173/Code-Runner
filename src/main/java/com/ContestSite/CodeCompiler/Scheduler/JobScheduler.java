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
        System.out.println("Schedular Started");
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(8, 8, 1,
                TimeUnit.MINUTES, taskQueue);
        executorService.prestartAllCoreThreads();

        new Thread(() -> {
            while(true) {
                if(taskQueue.size() <= 20) {
                    String jobId = pickJob();
                    if(jobId != null) {
                        Task task = Task.builder().jobId(jobId).build();
                        System.out.println("Adding Job to Queue : " + task.jobId);
                        taskQueue.add(task);
                    }
                }
            }
        }).start();
    }

    /**
     * Pops a User from UserQueue using UserQueueHandler
     * Picks a Job for this User from JobQueues using JobQueuesHandler
     * Pushes the User into the UserQueue if the User's Job Queue is not empty using UserQueueHandler
     */
    public synchronized static String pickJob() {
        String username = UserQueueHandler.pop();
        String jobId = JobQueuesHandler.pickUserOldestJob(username);
        if(!JobQueuesHandler.isUserQueueEmpty(username)) {
            UserQueueHandler.push(username);
        }
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
