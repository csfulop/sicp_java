package com.github.csfulop.sicpjava.lisp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.csfulop.sicpjava.lisp.ExpressionList.l;
import static com.github.csfulop.sicpjava.lisp.Token.t;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestLispEvaluator {
    private LispEvaluator evaluator;
    private Environment environment;

    @BeforeEach
    void setUp() {
        evaluator = new LispEvaluator();
        environment = new Environment();
    }

    @Test
    void testNumber() {
        // given
        Expression expression = t("12");
        // when
        Object result = evaluator.eval(expression, environment);
        // then
        assertThat(result, is(12));
    }

    @Test
    void testVariable() {
        // given
        Expression expression = t("a");
        environment.define("a", 12);
        // when
        Object result = evaluator.eval(expression, environment);
        // then
        assertThat(result, is(12));
    }

    @Test
    void testDefine() {
        // given
        Expression expression = l(t("define"), t("a"), t("12"));
        // when
        Object result = evaluator.eval(expression, environment);
        // then
        assertThat(environment.get("a").get(), is(12));
    }

    @Test
    void testAssignment() {
        // given
        environment.define("a", 11);
        Expression expression = l(t("set!"), t("a"), t("12"));
        // when
        Object result = evaluator.eval(expression, environment);
        // then
        assertThat(environment.get("a").get(), is(12));
    }
}
