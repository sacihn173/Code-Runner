package com.ContestSite.CodeCompiler.Executor;

import com.ContestSite.CodeCompiler.Entities.Job;
import com.ContestSite.CodeCompiler.Entities.ProgramLanguage;

public class JobExecutorJava extends JobExecutor {

    public void execute(Job job) {
        super.execute(job);
    }

    @Override
    public String getCommands(Job job) {
        return null;
    }

}
