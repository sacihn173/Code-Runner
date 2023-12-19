package com.ContestSite.CodeCompiler.Scheduler;

import com.ContestSite.CodeCompiler.Exceptions.QueueNotFoundException;
import com.ContestSite.CodeCompiler.Entities.Job;

import java.util.*;


public class JobQueues <K, T> {

    // QueueID (username) , Queue of JobIDs
    private Map<K, Queue<T>> queues;

    JobQueues() {
        queues = new HashMap<>();
    }

    public void insertJob(K queueId, T job) {
        if(!queues.containsKey(queueId)) {
            throw new QueueNotFoundException("Queue with id : " + queueId + " not found");
        }
        queues.get(queueId).add(job);
    }

    public void insertQueue(K queueId) {
        queues.put(queueId, new LinkedList<>());
    }

    public boolean containsQueueWithId(K queueId) {
        return queues.containsKey(queueId);
    }

    public synchronized T pickOldestJob(K queueId) {
        if (queues.containsKey(queueId)) {
            T job = queues.get(queueId).peek();
            queues.get(queueId).poll();
            return job;
        }
        return null;
    }

    public synchronized boolean isQueueEmpty(K queueId) {
        if(queues.containsKey(queueId)) {
            return queues.get(queueId).isEmpty();
        }
        return true;
    }

}
