package com.ContestSite.CodeCompiler.APIs;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.JobStatus;
import com.ContestSite.CodeCompiler.Scheduler.JobContextHandler;
import jakarta.ws.rs.QueryParam;
import lombok.Builder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class InspectJobAPI {

    @GetMapping("/status")
    public Response inspectJob(@QueryParam("jobId") String jobId) {
        /*
         * Check the status of Job in Completion Queue and get the output
         */
        JobStatus jobStatus = JobContextHandler.getJobStatus(jobId);
        if(!Objects.equals(jobStatus, JobStatus.COMPLETED)) {
            return Response.builder().jobId(jobId).status(jobStatus.toString()).build();
        } else {
            Job job = JobContextHandler.getJobContext(jobId);
            return Response.builder()
                    .jobId(jobId).status(jobStatus.toString())
                    .sourceCode(job.getProgram().getSourceCode())
                    .output(job.getProgram().getOutput())
                    .errors(job.getProgram().getErrors())
                    .programLanguage(job.getProgram().getProgramLanguage().toString())
                    .build();
        }
    }

    @Builder
    public static class Response {
        String jobId;
        String status;
        String sourceCode;
        String programLanguage;
        String output;
        String errors;
    }

}
