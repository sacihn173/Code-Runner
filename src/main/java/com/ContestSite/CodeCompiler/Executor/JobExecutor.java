package com.ContestSite.CodeCompiler.Executor;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.JobStatus;
import com.ContestSite.CodeCompiler.Scheduler.JobContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public abstract class JobExecutor {

    private final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

    public void execute(Job job, String fileExtension) {
        job.setJobStatus(JobStatus.RUNNING);
        JobContextHandler.updateJob(job.getJodId(), job);
        populateJobData(job, fileExtension);

        // TODO : Job Failure Handling Mechanism

        if(!createSourceCodeFile(job))
            throw new RuntimeException("Error while creating source file for sub id : " + job.getJodId());

        if(!createCommandsFile(job))
            throw new RuntimeException("Error while creating commands file for sub id : " + job.getJodId());

        if(!createOutputFile(job))
            throw new RuntimeException("Error while creating output file for sub id : " + job.getJodId());

        Process pc;
        String commandsFilePath = System.getProperty("user.dir") + "/" + job.getProgram().getCommandsFileName();
        try {
            pc = Runtime.getRuntime().exec("chmod +x " + commandsFilePath);
            pc.waitFor();
            pc = Runtime.getRuntime().exec(commandsFilePath);
            logger.info("Compilation started for id : " + job.getJodId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("error while creating process for id : " + job.getJodId());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long startTime = System.currentTimeMillis();
        while(pc.isAlive()) {
            long curTime = System.currentTimeMillis();
            // condition for a TLE
            if(curTime - startTime >= 4000) {
                pc.destroyForcibly();
                deleteCreatedFiles(job);
                job.getProgram().setTimeLimitExceeded(true);
                logger.info("Job exceeded time limit : " + job.getJodId());
                return;
            }
        }

        populateJobOutput(job);
        deleteCreatedFiles(job);
        job.setJobStatus(JobStatus.COMPLETED);
        JobContextHandler.updateJob(job.getJodId(), job);
        // TODO : Decide when to push the response
    }

    private void populateJobData(Job job, String fileExtension) {
        String sourceCodeFileName = job.getJodId() + "_SourceCode" + fileExtension;
        String outputFileName = job.getJodId() + "_OutputFile.txt";
        String errorFileName = job.getJodId() + "_ErrorFile.txt";
        String commandsFileName = job.getJodId() + "_CommandsFile.sh";

        job.getProgram().setSourceCodeFileName(sourceCodeFileName);
        job.getProgram().setOutputFileName(outputFileName);
        job.getProgram().setCommandsFileName(commandsFileName);
        job.getProgram().setErrorFileName(errorFileName);
    }

    public void deleteCreatedFiles(Job job) {
        try {
            (new File(job.getProgram().getSourceCodeFileName())).delete();
            (new File(job.getProgram().getErrorFileName())).delete();
            (new File(job.getProgram().getOutputFileName())).delete();
            (new File(job.getProgram().getSourceCodeFileExtension())).delete(); // delete the ./a.out type file
            (new File(job.getProgram().getCommandsFileName())).delete();
        } catch (Exception e) {
            logger.info("error while deleting files for source file : " + job.getProgram().getSourceCodeFileName());
            e.printStackTrace();
        }
    }

    public boolean createSourceCodeFile(Job job) {
        String sourceCodeFileName = job.getProgram().getSourceCodeFileName();
        File sourceCode = new File(sourceCodeFileName);
        FileWriter sourceCodeWriter;
        try {
            sourceCode.createNewFile();
            sourceCodeWriter = new FileWriter(sourceCode);
            sourceCodeWriter.write(job.getProgram().getSourceCode());
            sourceCodeWriter.close();
        } catch (IOException e) {
            logger.info("error while creating source code file for id : " + job.getJodId());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createOutputFile(Job job) {
        File codeOutput = new File(job.getProgram().getOutputFileName());
        File errorFile = new File(job.getProgram().getErrorFileName());
        try {
            codeOutput.createNewFile();
            errorFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createCommandsFile(Job job) {
        File commandsFile = new File(job.getProgram().getCommandsFileName());
        FileWriter commandsFileWriter;
        try {
            commandsFile.createNewFile();
            commandsFileWriter = new FileWriter(job.getProgram().getCommandsFileName());
            String commands = this.getCommands(job);
            commandsFileWriter.write(commands);
            commandsFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void populateJobOutput(Job job) {
        StringBuilder output = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(job.getProgram().getOutputFileName()));
            String iteratorString;
            logger.info("Execution success, fetching output for id : " + job.getJodId());
            while((iteratorString = bufferedReader.readLine()) != null) {
                output.append(iteratorString);
                output.append("\n");
            }
            bufferedReader.close();

            BufferedReader errorFileReader = new BufferedReader(new FileReader(job.getProgram().getErrorFileName()));
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
        job.getProgram().setOutput(String.valueOf(output));
        job.getProgram().setErrors(String.valueOf(errors));
    }

    public abstract String getCommands(Job job);

}
