package com.github.csfulop.sicpjava.lisp.functions;

import com.github.csfulop.sicpjava.lisp.BuiltInFunction;
import com.github.csfulop.sicpjava.lisp.exceptions.LispException;

public class Sub implements BuiltInFunction {
    @Override
    public Object run(Object... arguments) {
        if (arguments.length < 1) {
            throw new LispException("The procedure has been called with 0 arguments; it requires at least 1 argument.");
        }
        if (arguments.length == 1) {
            return -((Integer) arguments[0]);
        }
        int result = (Integer) arguments[0];
        for (int i = 1; i < arguments.length; i++) {
            result -= (Integer) arguments[i];
        }
        return result;
    }
}
