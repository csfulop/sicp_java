package com.github.csfulop.sicpjava.lisp.functions;

import com.github.csfulop.sicpjava.lisp.BuiltInFunction;

public class Add implements BuiltInFunction {
    @Override
    public Object run(Object... arguments) {
        int result = 0;
        for (Object arg : arguments) {
            result += (Integer) arg;
        }
        return result;
    }
}
