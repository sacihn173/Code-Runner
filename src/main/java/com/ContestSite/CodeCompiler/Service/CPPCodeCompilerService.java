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
        // 1. create a file with source code

        if(!createSourceCodeFile(request))
            return ResponseEntity.ok().body("Error Creating source file");

        // 2. create a file with command to run compile, run and add input

        if(!createCommandsFile(request.getTestCase()))
            return ResponseEntity.ok().body("Error creating commands file");

        // 3. create a file to take output

        if(!createOutputFile())
            return ResponseEntity.ok().body("Error creating output file");

        // 4. execute the file created in 2 step

        ProcessBuilder processBuilder = new ProcessBuilder("cmd");
        processBuilder.redirectInput(new File("commandsFile.txt"));
        Process pc;

        try {
            pc = processBuilder.start();
            logger.info("Compilation started");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long startTime = System.currentTimeMillis();
        while(pc.isAlive()) {
            long curTime = System.currentTimeMillis();
            if(curTime - startTime >= 4000) {
                pc.destroyForcibly();
                try {
                    Runtime.getRuntime().exec("taskkill /IM sourceCode.exe /F");
                    logger.info("Task killed forcefully");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//                (new File("codeOutput.txt")).delete();
                return ResponseEntity.ok().body("Time Limit Exceeded");
            }
        }

        return getProgramOutput();
    }

    public boolean createSourceCodeFile(CustomRunRequest request) {
        File sourceCode = new File("sourceCode.cpp");
        FileWriter sourceCodeWriter;
        File previousExeFile = new File("sourceCode.exe");
        try {
            previousExeFile.delete();
            if(!sourceCode.createNewFile())
                sourceCode.delete();
            sourceCode.createNewFile();
            sourceCodeWriter = new FileWriter("sourceCode.cpp");
            sourceCodeWriter.write(request.getCode());
            sourceCodeWriter.close();
            logger.info("Source code file ready");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** Program is run twice, once for errors, once for output, make two into one command */
    private boolean createCommandsFile(String testCase) {
        File commandsFile = new File("commandsFile.txt");
        FileWriter commandsFileWriter;
        try {
            commandsFile.createNewFile();
            commandsFileWriter = new FileWriter("commandsFile.txt");
            commandsFileWriter.write("g++ -o sourceCode sourceCode.cpp\n");
            commandsFileWriter.write("sourceCode.exe > codeOutput.txt\n");
            commandsFileWriter.write(testCase);
            commandsFileWriter.write("\n");
            commandsFileWriter.write("g++ sourceCode.cpp 2>errors.txt\n");
            commandsFileWriter.write(testCase);
            commandsFileWriter.close();
            logger.info("Commands file ready");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean createOutputFile() {
        File codeOutput = new File("codeOutput.txt");
        File errorFile = new File("errors.txt");
        try {
            if(!codeOutput.createNewFile())
                codeOutput.delete();
            codeOutput.createNewFile();
            if(!errorFile.createNewFile())
                errorFile.delete();
            errorFile.createNewFile();
            logger.info("Output and error file ready");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private ResponseEntity<?> getProgramOutput() {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("codeOutput.txt"));
            String iteratorString;
            logger.info("Reading the output file");
            while((iteratorString = bufferedReader.readLine()) != null) {
                output.append(iteratorString);
                output.append("\n");
            }
            bufferedReader.close();

            BufferedReader errorFileReader = new BufferedReader(new FileReader("errors.txt"));
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
