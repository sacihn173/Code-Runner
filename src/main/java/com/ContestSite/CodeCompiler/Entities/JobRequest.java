package com.ContestSite.CodeCompiler.Entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest extends Job {

//    @NotNull
    private String username;

}
