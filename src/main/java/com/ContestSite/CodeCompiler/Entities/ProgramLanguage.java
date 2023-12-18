package com.ContestSite.CodeCompiler.Entities;

import com.ContestSite.CodeCompiler.Executor.JobExecutor;
import com.ContestSite.CodeCompiler.Executor.JobExecutorCpp;
import com.ContestSite.CodeCompiler.Executor.JobExecutorJava;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProgramLanguage {

    CPP("cpp", ".cpp", JobExecutorCpp.class),
    JAVA("java", ".java", JobExecutorJava.class);

    private final String languageName;

    private final String fileExtension;

    private final Class<? extends JobExecutor> executorClass;

}
