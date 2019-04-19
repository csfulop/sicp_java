package com.github.csfulop.sicpjava.lisp;

import org.junit.jupiter.api.Test;

import static com.github.csfulop.sicpjava.lisp.ExpressionList.l;
import static com.github.csfulop.sicpjava.lisp.Token.t;

public class TestExpression {
    @Test
    void testNumber() {
        Expression expression = t("12");
    }

    void testDefine() {
        Expression expression = l(t("define"), t("a"), t("12"));
    }
}
