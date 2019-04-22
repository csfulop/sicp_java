package com.github.csfulop.sicpjava.lisp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TestReadEvalPrintLoop {
    private LispEvaluator evaluator;
    private Environment environment;
    private Parser parser;
    private ReadEvalPrintLoop repl;

    @BeforeEach
    void setUp() {
        evaluator = new LispEvaluator();
        environment = new Environment();
        parser = new Parser();
        repl = new ReadEvalPrintLoop();
    }

    @Test
    void testDefineSetGetVariable() {
        repl.eval("(define a 11)");
        assertThat(repl.eval("a"), is(11));
        repl.eval("(set! a 12)");
        assertThat(repl.eval("a"), is(12));
        assertThat(repl.evalPrint("a"), is(12));
    }

    @Test
    void testAdd() {
        assertThat(repl.eval("(+ 1 2)"), is(3));
    }

    @Test
    void testTwoDeepAdd() {
        assertThat(repl.eval("(+ 1 (+ 2 3))"), is(6));
    }

    @Test
    void testAddVariables() {
        repl.eval("(define a 1)");
        assertThat(repl.eval("(+ a 2 3)"), is(6));
    }

    @Test
    void testReadFromInputStream() {
        String code = "(define a 1)\n" +
                "(+ a 2)";
        InputStream inputStream = new ByteArrayInputStream(code.getBytes(Charset.forName("UTF-8")));
        assertThat(repl.readEvalPrintLoop(inputStream), is(3));
    }

    @Test
    void testMultiplyVariables() {
        repl.eval("(define a 3)");
        assertThat(repl.eval("(* a 2 4)"), is(24));
    }

    @Test
    void testAddAndMultiplyVariables() {
        repl.eval("(define a 2)");
        repl.eval("(define b 3)");
        assertThat(repl.eval("(* (+ 1 b) (+ a 2) 2)"), is(32));
    }



}
