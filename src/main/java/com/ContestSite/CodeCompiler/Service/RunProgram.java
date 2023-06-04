package com.ContestSite.CodeCompiler.Service;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import com.ContestSite.CodeCompiler.Models.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public abstract class RunProgram {

    private final Logger logger = LoggerFactory.getLogger(RunProgram.class);

    public abstract String getCommands(Program program);

    public CustomRunResponse runProgram(CustomRunRequest request, String fileExtension) {

        String sourceCodeFileName = request.getUniqueSubmissionId() + "_SourceCode" + fileExtension;
        String outputFileName = request.getUniqueSubmissionId() + "_OutputFile.txt";
        String errorFileName = request.getUniqueSubmissionId() + "_ErrorFile.txt";
        String commandsFileName = request.getUniqueSubmissionId() + "_CommandsFile.sh";
        String sourceCodeFileNameWithoutExtension = request.getUniqueSubmissionId() + "_SourceCode";

        Program program = new Program(request.getUniqueSubmissionId(), sourceCodeFileName, sourceCodeFileNameWithoutExtension,
                outputFileName, errorFileName, commandsFileName, request.getCode(), request.getTestCase());

        if(!createSourceCodeFile(program))
            throw new RuntimeException("Error while creating source file for sub id : " + request.getUniqueSubmissionId());

        if(!createCommandsFile(program))
            throw new RuntimeException("Error while creating commands file for sub id : " + request.getUniqueSubmissionId());

        if(!createOutputFile(program))
            throw new RuntimeException("Error while creating output file for sub id : " + request.getUniqueSubmissionId());

        Process pc;
        String commandsFilePath = System.getProperty("user.dir") + "/" + commandsFileName;
        try {
            pc = Runtime.getRuntime().exec("chmod +x " + commandsFilePath);
            pc.waitFor();
            pc = Runtime.getRuntime().exec(commandsFilePath);
            logger.info("Compilation started for id : " + request.getUniqueSubmissionId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error while creating process for id : " + request.getUniqueSubmissionId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long startTime = System.currentTimeMillis();
        while(pc.isAlive()) {
            long curTime = System.currentTimeMillis();
            // condition for a TLE
            if(curTime - startTime >= 4000) {
                pc.destroyForcibly();
                deleteCreatedFiles(program);
                return CustomRunResponse.builder().errors("Time Limit Exceeded").build();
            }
        }

        CustomRunResponse response = getProgramOutput(program);
        deleteCreatedFiles(program);
        return response;
    }
    public void deleteCreatedFiles(Program program) {
        try {
            (new File(program.getSourceCodeFileName())).delete();
            (new File(program.getErrorFileName())).delete();
            (new File(program.getOutputFileName())).delete();
            (new File(program.getSourceCodeFileExtension())).delete(); // delete the ./a.out type file
            (new File(program.getCommandsFileName())).delete();
        } catch (Exception e) {
            logger.info("error while deleting files for source file : " + program.getSourceCodeFileName());
            e.printStackTrace();
        }
    }

    public boolean createSourceCodeFile(Program program) {
        String sourceCodeFileName = program.getSourceCodeFileName();
        File sourceCode = new File(sourceCodeFileName);
        FileWriter sourceCodeWriter;
        try {
            sourceCode.createNewFile();
            sourceCodeWriter = new FileWriter(sourceCode);
            sourceCodeWriter.write(program.getSourceCode());
            sourceCodeWriter.close();
        } catch (IOException e) {
            logger.info("error while creating source code file for id : " + program.getSubmissionId());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createOutputFile(Program program) {
        File codeOutput = new File(program.getOutputFileName());
        File errorFile = new File(program.getErrorFileName());
        try {
            codeOutput.createNewFile();
            errorFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createCommandsFile(Program program) {
        File commandsFile = new File(program.getCommandsFileName());
        FileWriter commandsFileWriter;
        try {
            commandsFile.createNewFile();
            commandsFileWriter = new FileWriter(program.getCommandsFileName());
            String commands = this.getCommands(program);
            commandsFileWriter.write(commands);
            commandsFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public CustomRunResponse getProgramOutput(Program program) {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(program.getOutputFileName()));
            String iteratorString;
            logger.info("Execution success, fetching output for id : " + program.getSubmissionId());
            while((iteratorString = bufferedReader.readLine()) != null) {
                output.append(iteratorString);
                output.append("\n");
            }
            bufferedReader.close();

            BufferedReader errorFileReader = new BufferedReader(new FileReader(program.getErrorFileName()));
            while ((iteratorString = errorFileReader.readLine()) != null) {
                errors.append(iteratorString);
                errors.append("\n");
            }
            errorFileReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // removing '/n' from end
        if(output.length() >= 2) output.delete(output.length()-1, output.length());
        if(errors.length() >= 2) errors.delete(errors.length()-1, errors.length());
        return CustomRunResponse.builder()
                .output(String.valueOf(output))
                .errors(String.valueOf(errors))
                .build();
    }

}
