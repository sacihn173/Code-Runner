package com.ContestSite.CodeCompiler.APIs;

import com.ContestSite.CodeCompiler.Executor.JobExecutorCpp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private JobExecutorCpp runCppService;

//    @PostMapping("/custom-run")
//    public CustomRunResponse runCode(@RequestBody CustomRunRequest request) {
//        return runCppService.runCPPTask(request);
//    }

}
