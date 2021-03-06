package com.github.csfulop.sicpjava.lisp.functions;

import com.github.csfulop.sicpjava.lisp.BuiltInFunction;

public class Equals implements BuiltInFunction {
    @Override
    public Object run(Object... arguments) {
        if (arguments.length < 2) {
            return true;
        }
        for (int i = 0; i < arguments.length - 1; i++) {
            if (arguments[i] != arguments[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
