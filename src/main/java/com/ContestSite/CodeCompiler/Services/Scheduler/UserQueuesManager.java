package com.ContestSite.CodeCompiler.Services.Scheduler;

import com.ContestSite.CodeCompiler.Models.Job;

public class UserQueuesManager {

    private static UserQueuesManager userQueuesManager;

    private static Queues<Job> queues;

    private UserQueuesManager() {
        queues = new Queues<>();
    }

    public static UserQueuesManager getInstance() {
        if(queues == null) {
            return new UserQueuesManager();
        } else {
            return userQueuesManager;
        }
    }

    public boolean containsUserQueue(String userId) {
        return queues.containsQueueWithId(userId);
    }

    public void insertQueue(String queueId) {
        queues.insertQueue(queueId);
    }

    public void insertUserJob(String queueId, Job job) {
        queues.insertJob(queueId, job);
    }

}
