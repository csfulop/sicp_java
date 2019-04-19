package com.github.csfulop.sicpjava.lisp;

import lombok.Data;
import lombok.NonNull;

@Data
public class Token extends Expression {

    public static Token t(String value) {
        return new Token(value);
    }

    @NonNull
    private final String value;
}
