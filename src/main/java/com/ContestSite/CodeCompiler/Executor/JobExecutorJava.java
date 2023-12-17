package com.ContestSite.CodeCompiler.Executor;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.ProgramLanguage;

public class JobExecutorJava extends JobExecutor {

    private static final String FILE_EXTENSION = ProgramLanguage.JAVA.getFileExtension();

    public void execute(Job job) {
        execute(job, FILE_EXTENSION);
    }

    @Override
    public String getCommands(Job job) {
        return null;
    }

}
