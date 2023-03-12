package com.ContestSite.CodeCompiler.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomRunRequest {

    private String code;

    private String codeLanguage;

    private String testCase;

}
