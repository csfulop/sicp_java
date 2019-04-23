package com.github.csfulop.sicpjava.lisp.functions;

import com.github.csfulop.sicpjava.lisp.BuiltInFunction;

public class Mul implements BuiltInFunction {
    @Override
    public Object run(Object... arguments) {
        int result = 1;
        for (Object arg : arguments) {
            result *= (Integer) arg;
        }
        return result;
    }
}
