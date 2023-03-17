package com.ContestSite.CodeCompiler.Service;

import com.ContestSite.CodeCompiler.Models.CustomRunRequest;
import com.ContestSite.CodeCompiler.Models.CustomRunResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.logging.Logger;

@Service
public class CPPCodeCompilerService {

    private Logger logger = Logger.getLogger(CPPCodeCompilerService.class.getName());

    /** To run and get output from a C++ file */
    public ResponseEntity<?> runCPPFile(CustomRunRequest request) {
        String sourceCodeFileName = request.getUniqueSubmissionId() + "_SourceCode.cpp";
        String outputFileName = request.getUniqueSubmissionId() + "_OutputFile.txt";
        String errorFileName = request.getUniqueSubmissionId() + "_ErrorFile.txt";
        String commandsFileName = request.getUniqueSubmissionId() + "_CommandsFile.txt";

        if(!createSourceCodeFile(request, sourceCodeFileName))
            return ResponseEntity.ok().body("Error Creating source file for id : " + request.getUniqueSubmissionId());

        if(!createCommandsFile(request.getTestCase(), commandsFileName, outputFileName,
                errorFileName, sourceCodeFileName))
            return ResponseEntity.ok().body("Error creating commands file for id : " + request.getUniqueSubmissionId());

        if(!createOutputFile(outputFileName, errorFileName))
            return ResponseEntity.ok().body("Error creating output file for id : " + request.getUniqueSubmissionId());

        // execute the source code
        ProcessBuilder processBuilder = new ProcessBuilder("cmd");
        processBuilder.redirectInput(new File(commandsFileName));
        Process pc;

        try {
            pc = processBuilder.start();
            logger.info("Compilation started for id : " + request.getUniqueSubmissionId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long startTime = System.currentTimeMillis();
        while(pc.isAlive()) {
            long curTime = System.currentTimeMillis();
            // condition for a TLE
            if(curTime - startTime >= 4000) {
                pc.destroyForcibly();
                try {
                    Runtime.getRuntime().exec("taskkill /IM sourceCode.exe /F");
                    logger.info("Task killed forcefully for id : " + request.getUniqueSubmissionId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                deleteCreatedFiles(sourceCodeFileName, errorFileName, outputFileName, commandsFileName);
                return ResponseEntity.ok().body("Time Limit Exceeded");
            }
        }

        ResponseEntity<?> response = getProgramOutput(request.getUniqueSubmissionId(), outputFileName, errorFileName);
        deleteCreatedFiles(sourceCodeFileName, errorFileName, outputFileName, commandsFileName);
        return response;
    }

    private void deleteCreatedFiles(String sourceCodeFileName, String errorsFileName, String outputFileName,
                                    String commandsFileName) {
        String exeFileName = sourceCodeFileName.substring(0, sourceCodeFileName.length()-4) + ".exe";
        (new File(sourceCodeFileName)).delete();
        (new File(errorsFileName)).delete();
        (new File(outputFileName)).delete();
        (new File(exeFileName)).delete();
        (new File(commandsFileName)).delete();
    }

    public boolean createSourceCodeFile(CustomRunRequest request, String sourceCodeFileName) {

        File sourceCode = new File(sourceCodeFileName);
        FileWriter sourceCodeWriter;
        try {
            sourceCode.createNewFile();
            sourceCodeWriter = new FileWriter(sourceCode);
            sourceCodeWriter.write(request.getCode());
            sourceCodeWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** Program is run twice, once for errors, once for output, make two into one command */
    private boolean createCommandsFile(String testCase, String commandsFileName, String outputFileName,
                                       String errorFileName, String sourceCodeFileName) {
        String sourceCodeFileNameWithoutCpp = sourceCodeFileName.substring(0, sourceCodeFileName.length()-4);
        File commandsFile = new File(commandsFileName);
        FileWriter commandsFileWriter;
        try {
            commandsFile.createNewFile();
            commandsFileWriter = new FileWriter(commandsFileName);
            commandsFileWriter.write("g++ -o " + sourceCodeFileNameWithoutCpp + " " + sourceCodeFileName +" \n");
            commandsFileWriter.write(sourceCodeFileNameWithoutCpp + ".exe > " + outputFileName + "\n");
            commandsFileWriter.write(testCase);
            commandsFileWriter.write("\n");
            commandsFileWriter.write("g++ " + sourceCodeFileName + " 2> "+ errorFileName + "\n");
            commandsFileWriter.write(testCase);
            commandsFileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createOutputFile(String outputFileName, String errorFileName) {
        File codeOutput = new File(outputFileName);
        File errorFile = new File(errorFileName);
        try {
            codeOutput.createNewFile();
            errorFile.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private ResponseEntity<?> getProgramOutput(String uniqueSubmissionId, String outputFileName, String errorsFileName) {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(outputFileName));
            String iteratorString;
            logger.info("Execution success, fetching output for id : " + uniqueSubmissionId);
            while((iteratorString = bufferedReader.readLine()) != null) {
                output.append(iteratorString);
                output.append("\n");
            }
            bufferedReader.close();

            BufferedReader errorFileReader = new BufferedReader(new FileReader(errorsFileName));
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

        return ResponseEntity.ok().body(
                CustomRunResponse.builder()
                        .output(String.valueOf(output))
                        .errors(String.valueOf(errors))
                        .build()
        );
    }

}
