package com.ContestSite.CodeCompiler.Executor;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.ProgramLanguage;
import org.springframework.stereotype.Service;

@Service
public class JobExecutorCpp extends JobExecutor {

    private static final String FILE_EXTENSION = ProgramLanguage.CPP.getFileExtension();

    public void execute(Job job) {
        this.execute(job, FILE_EXTENSION);
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
