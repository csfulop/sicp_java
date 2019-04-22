package com.github.csfulop.sicpjava.lisp.functions;

import com.github.csfulop.sicpjava.lisp.Function;

public class Mul implements Function {
    @Override
    public Object run(Object... arguments) {
        int result = 1;
        for (Object arg : arguments) {
            result *= (Integer) arg;
        }
        return result;
    }
}
