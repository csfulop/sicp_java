package com.github.csfulop.sicpjava.lisp;

import com.github.csfulop.sicpjava.lisp.exceptions.ParserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.csfulop.sicpjava.lisp.ExpressionList.l;
import static com.github.csfulop.sicpjava.lisp.Token.t;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestParser {
    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    @Test
    void tokenizeInt() {
        // given
        // when
        List<String> tokens = parser.tokenize("12");
        // then
        assertThat(tokens, is(asList("12")));
    }

    @Test
    void tokenizeList() {
        // given
        // when
        List<String> tokens = parser.tokenize("(set! a 12)");
        // then
        assertThat(tokens, is(asList("(", "set!", "a", "12", ")")));
    }

    @Test
    void tokenizeDoubleDeepList() {
        // given
        // when
        List<String> tokens = parser.tokenize("((a b 12) c (d))");
        // then
        assertThat(tokens, is(asList("(", "(", "a", "b", "12", ")", "c", "(", "d", ")", ")")));
    }

    @Test
    void parseInt() {
        // given
        // when
        Expression expression = parser.parse("12");
        // then
        assertThat(expression, is(t("12")));
    }

    @Test
    void parseList() {
        // given
        // when
        Expression expression = parser.parse("(set! a 12)");
        // then
        assertThat(expression, is(l(t("set!"), t("a"), t("12"))));
    }

    @Test
    void parseEmptyList() {
        // given
        // when
        Expression expression = parser.parse("()");
        // then
        assertThat(expression, is(l()));
    }

    @Test
    void parseListWithOneToken() {
        // given
        // when
        Expression expression = parser.parse("(a)");
        // then
        assertThat(expression, is(l(t("a"))));
    }

    @Test
    void parseDoubleDeepList() {
        // given
        // when
        Expression expression = parser.parse("((a b 12) c (d))");
        // then
        assertThat(expression, is(l(l(t("a"), t("b"), t("12")), t("c"), l(t("d")))));
    }

    @Test
    void parseDoubleDeepEmptyList() {
        // given
        // when
        Expression expression = parser.parse("(())");
        // then
        assertThat(expression, is(l(l())));
    }

    @Test
    void canTokenizeInvalidExpression() {
        // given
        // when
        List<String> tokens = parser.tokenize(")");
        // then
        assertThat(tokens, is(asList(")")));
    }

    @Test
    void parserFailsForInvalidExpression() {
        assertThrows(ParserException.class, () -> parser.parse(")"));
        assertThrows(ParserException.class, () -> parser.parse(""));
        assertThrows(ParserException.class, () -> parser.parse("("));
    }
}
