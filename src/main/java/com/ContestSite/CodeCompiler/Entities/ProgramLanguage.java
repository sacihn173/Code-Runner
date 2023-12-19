package com.ContestSite.CodeCompiler.Entities;

import com.ContestSite.CodeCompiler.Executor.JobExecutor;
import com.ContestSite.CodeCompiler.Executor.JobExecutorCpp;
import com.ContestSite.CodeCompiler.Executor.JobExecutorJava;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ProgramLanguage {

    UNDEFINED("undefined", "undefined", null),
    CPP("cpp", ".cpp", JobExecutorCpp.class),
    JAVA("java", ".java", JobExecutorJava.class);

    private final String languageName;

    private final String fileExtension;

    private final Class<? extends JobExecutor> executorClass;

    public static ProgramLanguage getLanguageByName(String languageName) {
        for(ProgramLanguage language : ProgramLanguage.values()) {
            if(language.getLanguageName().equals(languageName)) {
                return language;
            }
        }
        return UNDEFINED;
    }

}
