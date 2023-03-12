package com.ContestSite.CodeCompiler.Controllers;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Service.CPPCodeCompilerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HomeController {

    private final CPPCodeCompilerService codeCompilerService;

    /** Add support for multi-threading */
    @GetMapping("/custom-run")
    public ResponseEntity<?> runCode(@RequestBody CustomRunRequest request) {
        return codeCompilerService.runCPPFile(request);
    }


}
