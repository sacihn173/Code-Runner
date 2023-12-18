package com.ContestSite.CodeCompiler.Executor;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.ProgramLanguage;
import org.springframework.stereotype.Service;

@Service
public class JobExecutorCpp extends JobExecutor {

    public void execute(Job job) {
        super.execute(job);
    }

    @Override
    public String getCommands(Job job) {
        return "g++ -o " + job.getProgram().getSourceCodeFileExtension() + " "
                + job.getProgram().getSourceCodeFileName() +" 2> "
                + job.getProgram().getErrorFileName() + "\n" +
                "./" + job.getProgram().getSourceCodeFileExtension() + " > "
                + job.getProgram().getOutputFileName() + "\n" +
                job.getProgram().getTestcase();
    }

}
