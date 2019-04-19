package com.github.csfulop.sicpjava.lisp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestListEvaluatorAndParser {
    private LispEvaluator evaluator;
    private Environment environment;
    private Parser parser;

    @BeforeEach
    void setUp() {
        evaluator = new LispEvaluator();
        environment = new Environment();
        parser = new Parser();
    }

    @Test
    void test1() {
        evaluator.eval(parser.parse("(define a 11)"), environment);
        assertThat(evaluator.eval(parser.parse("a"), environment), is(11));
        evaluator.eval(parser.parse("(set! a 12)"), environment);
        assertThat(evaluator.eval(parser.parse("a"), environment), is(12));
    }
}
