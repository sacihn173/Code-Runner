package com.ContestSite.CodeCompiler.Scheduler;


public class JobQueuesHandler {

    private static JobQueues<String, String> jobQueues;

    static {
        jobQueues = new JobQueues<>();
    }

    public static boolean containsQueue(String id) {
        return jobQueues.containsQueueWithId(id);
    }

    public static void insertQueue(String id) {
        jobQueues.insertQueue(id);
    }

    public static void insertJob(String queueId, String jobId) {
        jobQueues.insertJob(queueId, jobId);
    }

    public static String pickUserOldestJob(String username) {
        return jobQueues.pickOldestJob(username);
    }

}
