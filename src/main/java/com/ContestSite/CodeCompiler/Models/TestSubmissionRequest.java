package com.ContestSite.CodeCompiler.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TestSubmissionRequest {
    private String codeLanguage;

    private String code;

    private List<String> testCases;

    private List<String> expectedOutputs;

}
