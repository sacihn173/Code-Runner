package com.ContestSite.CodeCompiler.Services.Scheduler;

import com.ContestSite.CodeCompiler.Exceptions.QueueNotFoundException;
import com.ContestSite.CodeCompiler.Models.Job;

import java.util.*;


public class Queues<T extends Job> {

    // queueId, queue
    private Map<String, Queue<T>> queues;

    Queues() {
        queues = new HashMap<>();
    }

    public void insertJob(String queueId, T job) {
        if(!queues.containsKey(queueId)) {
            throw new QueueNotFoundException("Queue with id : " + queueId + " not found");
        }
        queues.get(queueId).add(job);
    }

    public void insertQueue(String queueId) {
        queues.put(queueId, new LinkedList<>());
    }

    public boolean containsQueueWithId(String queueId) {
        return queues.containsKey(queueId);
    }

}
