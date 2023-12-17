package com.ContestSite.CodeCompiler.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProgramLanguage {

    JAVA("java", ".java"),
    CPP("cpp", ".cpp");

    private final String languageName;

    private final String fileExtension;

}
