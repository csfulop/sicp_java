package com.github.csfulop.sicpjava.lisp;

import lombok.NonNull;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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
        } else if (isIf(expression)) {
            return evalIf(expression, environment);
        } else if (isAssignment(expression)) {
            evalAssignment(expression, environment);
        } else if (isBegin(expression)) {
            return evalSequence(beginActions(expression), environment);
        } else if (isApplication(expression)) {
            return apply(
                    (Function) eval(operator(expression), environment),
                    evalValues(operands(expression), environment));
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

    private boolean isIf(Expression expression) {
        return isTaggedList(expression, "if");
    }

    private Object evalIf(Expression expression, Environment environment) {
        if ((Boolean) eval(ifPredicate(expression), environment)) {
            return eval(ifConsequent(expression), environment);
        } else {
            return eval(ifAlternative(expression), environment);
        }
    }

    private Expression ifPredicate(Expression expression) {
        return expressionCdr(expression)[0];
    }

    private Expression ifConsequent(Expression expression) {
        return expressionCdr(expression)[1];
    }

    private Expression ifAlternative(Expression expression) {
        if (expressionCdr(expression).length > 2) {
            return expressionCdr(expression)[2];
        } else {
            return null;
        }
    }


    private boolean isBegin(Expression expression) {
        return isTaggedList(expression, "begin");
    }

    private Expression[] beginActions(Expression expression) {
        return expressionCdr(expression);
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

    @NonNull
    private Expression operator(Expression expression) {
        return expressionCar(expression);
    }

    private Expression[] operands(Expression expression) {
        return expressionCdr(expression);
    }

    private Object[] evalValues(Expression[] operands, Environment environment) {
        return stream(operands).
                map(o -> eval(o, environment)).
                collect(Collectors.toList()).
                toArray();
    }


    private Expression[] expressionCdr(Expression expression) {
        LinkedList<@NonNull Expression> expressionList =
                new LinkedList<>(Arrays.asList(((ExpressionList) expression).getExpressions()));
        expressionList.remove(0);
        return expressionList.toArray(new Expression[0]);
    }

    @NonNull
    private Expression expressionCar(Expression expression) {
        return ((ExpressionList) expression).getExpressions()[0];
    }

    private boolean isTaggedList(Expression expression, String tag) {
        return expression instanceof ExpressionList
                && (expressionCar(expression) instanceof Token)
                && ((Token) expressionCar(expression)).getValue().equals(tag);
    }
}
