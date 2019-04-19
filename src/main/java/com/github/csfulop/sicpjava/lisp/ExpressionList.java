package com.github.csfulop.sicpjava.lisp;

import lombok.Data;
import lombok.NonNull;

@Data
public class ExpressionList extends Expression {

    public static ExpressionList l(Expression... expressions) {
        return new ExpressionList(expressions);
    }

    @NonNull
    private Expression[] expressions;
}
