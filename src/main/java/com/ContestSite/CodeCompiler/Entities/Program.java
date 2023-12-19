package com.ContestSite.CodeCompiler.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Program {

    private String sourceCode;

    private ProgramLanguage programLanguage;

    private String testcase;

    private String errors;

    private String output;

    private String sourceCodeFileExtension;

    private String sourceCodeFileName;

    private String outputFileName;

    private String errorFileName;

    private String commandsFileName;

    private boolean timeLimitExceeded = false;

}
