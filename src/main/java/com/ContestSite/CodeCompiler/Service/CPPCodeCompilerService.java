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

    public CustomRunResponse runCPPFile(CustomRunRequest request) {
        String sourceCodeFileName = request.getUniqueSubmissionId() + "_SourceCode.cpp";
        String outputFileName = request.getUniqueSubmissionId() + "_OutputFile.txt";
        String errorFileName = request.getUniqueSubmissionId() + "_ErrorFile.txt";
        String commandsFileName = request.getUniqueSubmissionId() + "_CommandsFile.sh";

        if(!createSourceCodeFile(request, sourceCodeFileName))
            throw new RuntimeException("Error while creating source file for sub id : " + request.getUniqueSubmissionId());

        if(!createCommandsFile(request.getTestCase(), commandsFileName, outputFileName, errorFileName, sourceCodeFileName))
            throw new RuntimeException("Error while creating commands file for sub id : " + request.getUniqueSubmissionId());

        if(!createOutputFile(outputFileName, errorFileName))
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
                deleteCreatedFiles(sourceCodeFileName, errorFileName, outputFileName, commandsFileName);
                return CustomRunResponse.builder().errors("Time Limit Exceeded").build();
            }
        }

        CustomRunResponse response = getProgramOutput(request.getUniqueSubmissionId(), outputFileName, errorFileName);
        deleteCreatedFiles(sourceCodeFileName, errorFileName, outputFileName, commandsFileName);
        return response;
    }

    private void deleteCreatedFiles(String sourceCodeFileName, String errorsFileName, String outputFileName,
                                    String commandsFileName) {
        String sourceCodeWithoutCPP = sourceCodeFileName.substring(0, sourceCodeFileName.length()-4);
        try {
            (new File(sourceCodeFileName)).delete();
            (new File(errorsFileName)).delete();
            (new File(outputFileName)).delete();
            (new File(sourceCodeWithoutCPP)).delete(); // delete the ./a.out type file
            (new File(commandsFileName)).delete();
        } catch (Exception e) {
            logger.info("error while deleting files for source file : " + sourceCodeFileName);
            e.printStackTrace();
        }
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
            logger.info("error while creating source code file for id : " + request.getUniqueSubmissionId());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createCommandsFile(String testCase, String commandsFileName, String outputFileName,
                                       String errorFileName, String sourceCodeFileName) {
        String sourceCodeFileNameWithoutCpp = sourceCodeFileName.substring(0, sourceCodeFileName.length()-4);
        File commandsFile = new File(commandsFileName);
        FileWriter commandsFileWriter;
        try {
            commandsFile.createNewFile();
            commandsFileWriter = new FileWriter(commandsFileName);
            commandsFileWriter.write(
                    "g++ -o " + sourceCodeFileNameWithoutCpp + " " + sourceCodeFileName +" 2> " + errorFileName + "\n" +
                    "./" + sourceCodeFileNameWithoutCpp + " > " + outputFileName + "\n" +
                    testCase);
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

    private CustomRunResponse getProgramOutput(String uniqueSubmissionId, String outputFileName, String errorsFileName) {
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
        return CustomRunResponse.builder()
                .output(String.valueOf(output))
                .errors(String.valueOf(errors))
                .build();
    }

}
