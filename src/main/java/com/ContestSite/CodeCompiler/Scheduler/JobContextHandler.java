package com.ContestSite.CodeCompiler.Scheduler;


import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.JobStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores every Job present in the System and always reflected the latest status of the Job
 */

public class JobContextHandler {

    private static Map<String, Job> context;

    static {
        context = new HashMap<>();
    }

    public static void addJob(Job job) {
        context.put(job.getJodId(), job);
    }

    public static Job getJobContext(String jobId) {
        return context.get(jobId);
    }

    public static JobStatus getJobStatus(String jobId) {
        if(context.containsKey(jobId)) {
            return context.get(jobId).getJobStatus();
        } else {
            return null;
        }
    }

    public static void updateJob(String jobId, Job job) {
        context.put(jobId, job);
    }

}
