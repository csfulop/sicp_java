package com.github.csfulop.sicpjava.lisp;

import lombok.Data;
import lombok.NonNull;

@Data
public class CompoundProcedure {
    @NonNull
    private final String[] parameters;
    @NonNull
    private final Expression body;
    @NonNull
    private final Environment environment;
}
