package com.github.csfulop.sicpjava.lisp;

import com.github.csfulop.sicpjava.lisp.exceptions.LispException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestReadEvalPrintLoop {
    private ReadEvalPrintLoop repl;

    @BeforeEach
    void setUp() {
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

    @Test
    void testValueOfBeginIsTheLastExpression() {
        assertThat(repl.eval("(begin 10 20)"), is(20));
    }

    @Test
    void testEmptyBegin() {
        assertThat(repl.eval("(begin)"), is(nullValue()));
    }

    @Test
    void testBeginUsesParentEnvironment() {
        repl.eval("(begin (define a 1))");
        assertThat(repl.eval("a"), is(1));
    }

    @Test
    void testBeginCanReadParentEnvironment() {
        repl.eval("(define a 3)");
        assertThat(repl.eval("(begin 1 2 a)"), is(3));
    }

    @Test
    void testEmptyEquals() {
        assertThat(repl.eval("(=)"), is(true));
    }

    @Test
    void testOneArgumentEquals() {
        assertThat(repl.eval("(= 1)"), is(true));
    }

    @Test
    void testTwoArgumentsAreEqual() {
        assertThat(repl.eval("(= 1 1)"), is(true));
    }

    @Test
    void testTwoArgumentsAreNotEqual() {
        assertThat(repl.eval("(= 1 2)"), is(false));
    }

    @Test
    void testThreeArgumentsAreEqual() {
        assertThat(repl.eval("(= 2 2 2)"), is(true));
    }

    @Test
    void testThreeArgumentsAreNotEqual() {
        assertThat(repl.eval("(= 2 2 3)"), is(false));
    }

    @Test
    void testEqualsWithCalculatedArguments() {
        assertThat(repl.eval("(= (+ 1 2) (+ 2 1))"), is(true));
    }

    @Test
    void testSubWithZeroParameters() {
        LispException exception = assertThrows(LispException.class, () -> repl.eval("(-)"));
        assertThat(exception.getMessage(), is("The procedure has been called with 0 arguments; it requires at least 1 argument."));
    }

    @Test
    void testSub() {
        assertThat(repl.eval("(- 1)"), is(-1));
        assertThat(repl.eval("(- 10 1)"), is(9));
        assertThat(repl.eval("(- 10 1 2)"), is(7));
    }

    @Test
    void testIfTrue() {
        assertThat(repl.eval("(if (= 1 1) 2 3)"), is(2));
    }

    @Test
    void testIfWithoutElseTrue() {
        assertThat(repl.eval("(if (= 1 1) 2)"), is(2));
    }

    @Test
    void testIfFalse() {
        assertThat(repl.eval("(if (= 1 0) 2 3)"), is(3));
    }

    @Test
    void testIfWithoutElseFalse() {
        assertThat(repl.eval("(if (= 1 0) 2)"), is(nullValue()));
    }

    @Test
    void testIfBranchWithBegin() {
        assertThat(repl.eval("(if (= 1 (* 1 1)) (begin 1 2 (+ 1 1 1)))"), is(3));
    }

    @Test
    void testElseBranchWithBegin() {
        assertThat(repl.eval("(if (= 1 (* 1 0)) 1 (begin 1 2 (+ 1 1 1)))"), is(3));
    }

    @Test
    void testDefineConstantLambda() {
        repl.eval("(define f (lambda () 1))");
        assertThat(repl.eval("(f)"), is(1));
    }

    @Test
    void testDefineLambdaWithoutParameters() {
        repl.eval("(define f (lambda () (* 2 2)))");
        assertThat(repl.eval("(f)"), is(4));
    }

    @Test
    void testLambdaWithoutParametersButComplexBody() {
        repl.eval("(define f (lambda () (begin (* 1 1) (+ 1 1))))");
        assertThat(repl.eval("(f)"), is(2));
    }

    @Test
    void testInfiniteRecursion() {
        repl.eval("(define f (lambda () (f)))");
        assertThrows(StackOverflowError.class, () -> repl.eval("(f)"));
    }

    @Test
    void testDefineLambdaWithParameters() {
        repl.eval("(define f (lambda (a) (* a 2)))");
        assertThat(repl.eval("(f 2)"), is(4));
    }

    @Test
    void testFactorial() {
        repl.eval("(define factorial (lambda (n) (if (= n 1) 1 (* n (factorial (- n 1))))))");
        assertThat(repl.eval("(factorial 1)"), is(1));
        assertThat(repl.eval("(factorial 2)"), is(2));
        assertThat(repl.eval("(factorial 3)"), is(6));
        assertThat(repl.eval("(factorial 4)"), is(24));
        assertThat(repl.eval("(factorial 5)"), is(120));
    }

    @Test
    void testLambdaWithTwoParameters() {
        repl.eval("(define f (lambda (a b) (+ a b)))");
        assertThat(repl.eval("(f 1 2)"), is(3));
    }

    @Test
    void testClosure() {
        repl.eval("(define f (lambda (x) (lambda (y) (+ x y))))");
        repl.eval("(define g1 (f 1))");
        repl.eval("(define g2 (f 2))");
        assertThat(repl.eval("(g1 2)"), is(3));
        assertThat(repl.eval("(g1 3)"), is(4));
        assertThat(repl.eval("(g2 2)"), is(4));
        assertThat(repl.eval("(g2 3)"), is(5));
    }

    @Test
    @Disabled
    void testLambdaWithMultipleStatements() {
        repl.eval("(define f (lambda () 1 2))");
        assertThat(repl.eval("(f)"), is(2));
    }
}
