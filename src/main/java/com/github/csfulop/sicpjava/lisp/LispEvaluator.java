package com.github.csfulop.sicpjava.lisp;

import lombok.NonNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LispEvaluator {
    public Object eval(Expression expression, Environment environment) {
        Optional<Object> result = selfEvaluating(expression);
        if (result.isPresent()) {
            return result.get();
        }

        if (isVariable(expression)) {
            return environment.get(((Token) expression).getValue());
        } else if (isDefinition(expression)) {
            evalDefinition(expression, environment);
        } else if (isAssignment(expression)) {
            evalAssignment(expression, environment);
        } else if (isBegin(expression)) {
            return evalSequence(beginActions(expression), environment);
        } else if (isApplication(expression)) {
            ExpressionList list = (ExpressionList) expression;
            Function procedure = (Function) eval(list.getExpressions()[0], environment);
            LinkedList<@NonNull Expression> argumentList = new LinkedList<>(Arrays.asList(list.getExpressions()));
            argumentList.remove(0);
            List<Object> evaluatedArguments = argumentList.stream().map(o -> eval(o, environment)).collect(Collectors.toList());
            return apply(procedure, evaluatedArguments.toArray());
        }
        return null;
    }

    private Object apply(Function procedure, Object... arguments) {
        return procedure.run(arguments);
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

    private boolean isBegin(Expression expression) {
        return isTaggedList(expression, "begin");
    }

    private Expression[] beginActions(Expression expression) {
        return expressionTail(expression);
    }

    private Object evalSequence(Expression[] expressions, Environment environment) {
        Object result = null;
        for (Expression expression : expressions) {
            result = eval(expression, environment);
        }
        return result;
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

    private boolean isApplication(Expression expression) {
        return expression instanceof ExpressionList;
    }

    private Expression[] expressionTail(Expression expression) {
        LinkedList<@NonNull Expression> expressionList =
                new LinkedList<>(Arrays.asList(((ExpressionList) expression).getExpressions()));
        expressionList.remove(0);
        return expressionList.toArray(new Expression[0]);
    }

    private boolean isTaggedList(Expression expression, String tag) {
        return expression instanceof ExpressionList
                && (((ExpressionList) expression).getExpressions()[0] instanceof Token)
                && ((Token) ((ExpressionList) expression).getExpressions()[0]).getValue().equals(tag);
    }
}
