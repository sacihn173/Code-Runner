package com.ContestSite.CodeCompiler.Service;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import com.ContestSite.CodeCompiler.Models.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunProgramJava extends RunProgram {

    private final Logger logger = LoggerFactory.getLogger(RunProgramJava.class);

    private static final String FILE_EXTENSION_JAVA = ".java";

    public CustomRunResponse runJavaProgram(CustomRunRequest request) {
        return this.runProgram(request, FILE_EXTENSION_JAVA);
    }

    @Override
    public String getCommands(Program program) {
        return null;
    }

}
