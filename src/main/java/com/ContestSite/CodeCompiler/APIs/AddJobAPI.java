package com.ContestSite.CodeCompiler.APIs;

import com.ContestSite.CodeCompiler.Entities.JobRequest;
import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.JobStatus;
import com.ContestSite.CodeCompiler.Scheduler.JobContextHandler;
import com.ContestSite.CodeCompiler.Scheduler.JobQueuesHandler;
import lombok.Builder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddJobAPI {

    @PostMapping("/add")
    public Response addJob(@RequestBody JobRequest request) {
        /*
         * Queue Id is username
         * JobId is username + "_" + timeInMillis
         */

        String username = request.getUsername();
        String jobId = username + "_" + System.currentTimeMillis();
        if(!JobQueuesHandler.containsQueue(username)) {
            JobQueuesHandler.insertQueue(username);
        }
        Job job = Job.builder().jodId(jobId).program(request.getProgram()).build();
        JobQueuesHandler.insertJob(username, job);
        job.setJobStatus(JobStatus.QUEUED);
        JobContextHandler.addJob(job);
        // Job is inserted into the queue, Job status can now be checked through API call
        return Response.builder().jobId(jobId).build();
    }

    @Builder
    public static class Response {
        private String jobId;
    }

}

