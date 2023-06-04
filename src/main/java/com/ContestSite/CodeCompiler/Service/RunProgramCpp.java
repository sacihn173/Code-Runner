package com.ContestSite.CodeCompiler.Service;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import com.ContestSite.CodeCompiler.Models.Program;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.logging.Logger;

@Service
public class RunProgramCpp extends RunProgram {

    private final Logger logger = Logger.getLogger(RunProgramCpp.class.getName());

    private static final String FILE_EXTENSION_CPP = ".cpp";

    public CustomRunResponse runCPPProgram(CustomRunRequest request) {
       return this.runProgram(request, FILE_EXTENSION_CPP);
    }

    @Override
    public String getCommands(Program program) {
        return "g++ -o " + program.getSourceCodeFileExtension() + " " + program.getSourceCodeFileName() +" 2> " + program.getErrorFileName() + "\n" +
                "./" + program.getSourceCodeFileExtension() + " > " + program.getOutputFileName() + "\n" +
                program.getTestcase();
    }

}
