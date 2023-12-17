package com.ContestSite.CodeCompiler.Scheduler;

import com.ContestSite.CodeCompiler.Entities.Job;

public class JobQueuesHandler {

    private static JobQueues<Job> jobQueues;

    static {
        jobQueues = new JobQueues<>();
    }

    public static boolean containsQueue(String id) {
        return jobQueues.containsQueueWithId(id);
    }

    public static void insertQueue(String id) {
        jobQueues.insertQueue(id);
    }

    public static void insertJob(String queueId, Job job) {
        jobQueues.insertJob(queueId, job);
    }

}
