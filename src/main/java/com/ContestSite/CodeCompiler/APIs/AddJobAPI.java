package com.ContestSite.CodeCompiler.APIs;

import com.ContestSite.CodeCompiler.Entities.*;
import com.ContestSite.CodeCompiler.Scheduler.JobContextHandler;
import com.ContestSite.CodeCompiler.Scheduler.JobQueuesHandler;
import com.ContestSite.CodeCompiler.Scheduler.UserQueue;
import com.ContestSite.CodeCompiler.Scheduler.UserQueueHandler;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddJobAPI {

    @PostMapping("/add")
    public ResponseEntity<Response> addJob(@RequestBody Request request) {
        /*
         * Queue Id is username
         * JobId is username + "_" + timeInMillis
         */
        String username = request.getUsername();
        String jobId = username + "_" + System.currentTimeMillis();
        if(!JobQueuesHandler.containsQueue(username)) {
            JobQueuesHandler.insertQueue(username);
        }
        Job job = request.tranformToJob(jobId);

        JobQueuesHandler.insertJob(username, jobId);
        UserQueueHandler.push(username);
        job.setJobStatus(JobStatus.QUEUED);
        JobContextHandler.addJob(job);

        // Job is inserted into the queue, Job status can now be checked through API call
        return ResponseEntity.ok(Response.builder().jobId(jobId).build());
    }

    @Builder
    @Getter
    @Setter
    public static class Response {

        private String jobId;

    }

    @Getter
    public static class Request {

        private String username;

        private String sourceCode;

        private String programLanguage;

        private String testcase;

        public Job tranformToJob(String jobId) {
            return Job.builder()
                    .jobId(jobId)
                    .program(Program.builder()
                            .sourceCode(sourceCode)
                            .testcase(testcase)
                            .programLanguage(ProgramLanguage.getLanguageByName(programLanguage))
                            .build())
                    .build();
        }
    }
}

