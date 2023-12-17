package com.ContestSite.CodeCompiler.APIs;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import com.ContestSite.CodeCompiler.Models.Job;
import com.ContestSite.CodeCompiler.Services.Scheduler.UserQueuesManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddJobAPI {

    @GetMapping("/custom-run")
    public CustomRunResponse addJob(@RequestBody CustomRunRequest request) {
        String username = request.getUsername();
        UserQueuesManager userQueuesManager = UserQueuesManager.getInstance();
        if(!userQueuesManager.containsUserQueue(username)) {
            userQueuesManager.insertQueue(username);
        }
        userQueuesManager.insertUserJob(
                username, Job.builder().jodId(username).program(request.getProgram()).build());

        // Job is inserted into queue, pick it from there and get response here
    }

}
