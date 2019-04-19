package com.github.csfulop.sicpjava.lisp;

import java.util.Optional;

public class LispEvaluator {
    public Object eval(Expression expression, Environment environment) {
        Optional<Object> result = selfEvaluating(expression);
        if (result.isPresent()) {
            return result.get();
        }

        if (isVariable(expression)) {
            return environment.get(((Token) expression).getValue()).get();
        }

        if (isDefinition(expression)) {
            evalDefinition(expression, environment);
        }

        if (isAssignment(expression)) {
            evalAssignment(expression, environment);
        }
        return null;
    }

    private Optional<Object> selfEvaluating(Expression expression) {
        try {
            if (expression instanceof Token) {
                int i = Integer.parseInt(((Token) expression).getValue());
                return Optional.of(i);
            }
        } catch (NumberFormatException nfe) {
        }
        // FIXME: string?
        return Optional.empty();
    }

    private boolean isVariable(Expression expression) {
        if (expression instanceof Token) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDefinition(Expression expression) {
        return isTaggedList(expression, "define");
    }

    private void evalDefinition(Expression expression, Environment environment) {
        Expression[] tags = ((ExpressionList) expression).getExpressions();
        String key = ((Token) tags[1]).getValue();
        Object value;
        String valueString = ((Token) tags[2]).getValue();
        value = Integer.parseInt(valueString); // FIXME: eval value
        environment.define(key, value);
    }

    private boolean isAssignment(Expression expression) {
        return isTaggedList(expression, "set!");
    }

    private void evalAssignment(Expression expression, Environment environment) {
        Expression[] tags = ((ExpressionList) expression).getExpressions();
        String key = ((Token) tags[1]).getValue();
        Object value;
        String valueString = ((Token) tags[2]).getValue();
        value = Integer.parseInt(valueString); // FIXME: eval value
        environment.set(key, value);
    }

    private boolean isTaggedList(Expression expression, String tag) {
        return expression instanceof ExpressionList
                && (((ExpressionList) expression).getExpressions()[0] instanceof Token)
                && ((Token) ((ExpressionList) expression).getExpressions()[0]).getValue().equals(tag);
    }
}
