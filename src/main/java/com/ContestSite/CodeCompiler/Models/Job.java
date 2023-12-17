package com.ContestSite.CodeCompiler.Models;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    private String jodId;

    protected Program program;

}
