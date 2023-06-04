package com.ContestSite.CodeCompiler.Models;

public class Program {

    private String submissionId;
    private String sourceCodeFileName;
    private String sourceCodeFileExtension;
    private String outputFileName;
    private String errorFileName;
    private String commandsFileName;
    private String sourceCode;
    private String testcase;

    public Program(String submissionId, String sourceCodeFileName, String sourceCodeFileExtension, String outputFileName,
                   String errorFileName, String commandsFileName, String sourceCode, String testcase) {
        this.submissionId = submissionId;
        this.sourceCodeFileName = sourceCodeFileName;
        this.sourceCodeFileExtension = sourceCodeFileExtension;
        this.outputFileName = outputFileName;
        this.errorFileName = errorFileName;
        this.commandsFileName = commandsFileName;
        this.sourceCode = sourceCode;
        this.testcase = testcase;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public String getSourceCodeFileName() {
        return sourceCodeFileName;
    }

    public void setSourceCodeFileName(String sourceCodeFileName) {
        this.sourceCodeFileName = sourceCodeFileName;
    }

    public String getSourceCodeFileExtension() {
        return sourceCodeFileExtension;
    }

    public void setSourceCodeFileExtension(String sourceCodeFileExtension) {
        this.sourceCodeFileExtension = sourceCodeFileExtension;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getErrorFileName() {
        return errorFileName;
    }

    public void setErrorFileName(String errorFileName) {
        this.errorFileName = errorFileName;
    }

    public String getCommandsFileName() {
        return commandsFileName;
    }

    public void setCommandsFileName(String commandsFileName) {
        this.commandsFileName = commandsFileName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getTestcase() {
        return testcase;
    }

    public void setTestcase(String testcase) {
        this.testcase = testcase;
    }
}
