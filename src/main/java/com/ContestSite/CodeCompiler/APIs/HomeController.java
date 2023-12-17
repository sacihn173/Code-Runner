package com.ContestSite.CodeCompiler.Controllers;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import com.ContestSite.CodeCompiler.Service.RunProgramCpp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    @Autowired
    private RunProgramCpp runCppService;

    @PostMapping("/custom-run")
    public CustomRunResponse runCode(@RequestBody CustomRunRequest request) {
        return runCppService.runCPPProgram(request);
    }

}
