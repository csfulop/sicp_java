package com.github.csfulop.sicpjava.lisp;

import com.github.csfulop.sicpjava.lisp.exceptions.ParserException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.csfulop.sicpjava.lisp.ExpressionList.l;
import static com.github.csfulop.sicpjava.lisp.Token.t;


public class Parser {
    /**
     * Shift-Reduce parser to transform Lisp code to Abstract Syntax Tree
     *
     * @param code Lisp code
     * @return Abstract Syntax Tree
     */
    public Expression parse(String code) {
        Deque<Expression> stack = new ArrayDeque<>();
        for (String token : tokenize(code)) {
            if (token.equals(")")) {
                reduce(stack);
            } else {
                shift(stack, token);
            }
        }
        if (stack.size() != 1) {
            throw new ParserException("Invalid expression!");
        }
        Expression result = stack.pop();
        if (isListOpening(result)) {
            throw new ParserException("Invalid expression: missing closing bracket!");
        }
        return result;
    }

    /**
     * Shift step of the Shift-Reduce parser
     *
     * @param stack the stack use by the parser
     * @param token the actual token
     */
    private void shift(Deque<Expression> stack, String token) {
        stack.push(t(token));
    }

    /**
     * Reduce step of the Shift-Reduce parser.
     * Pop expressions from the stack (in backward order) until find the list opening token.
     * Create a new ListExpression from these expressions and push back to the top of the stack.
     *
     * @param stack the stack to be reduced
     */
    private void reduce(Deque<Expression> stack) {
        try {
            Deque<Expression> reduced = new ArrayDeque<>();
            Expression pop = stack.pop();
            while (!isListOpening(pop)) {
                reduced.push(pop);
                pop = stack.pop();
            }
            ExpressionList expressionList = l(reduced.toArray(new Expression[0]));
            stack.push(expressionList);
        } catch (NoSuchElementException e) {
            throw new ParserException("Invalid expression: missing opening bracket!");
        }
    }

    /**
     * Is the given expression a list opening token?
     *
     * @param expression an expression
     * @return the given expression is a list opening token
     */
    private boolean isListOpening(Expression expression) {
        return (expression instanceof Token) && ((Token) expression).getValue().equals("(");
    }

    /**
     * Transform Lisp code to list of tokens
     *
     * @param code
     * @return
     */
    List<String> tokenize(String code) {
        Pattern pattern = Pattern.compile("\\(|\\)|[^()\\s]+");
        Matcher m = pattern.matcher(code);
        List<String> result = new LinkedList<>();
        while (m.find()) {
            result.add(m.group());
        }
        return result;
    }
}
