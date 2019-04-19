package com.github.csfulop.sicpjava.lisp.functions;

import com.github.csfulop.sicpjava.lisp.Function;

public class Add implements Function {
    @Override
    public Object run(Object... arguments) {
        int result = 0;
        for (Object arg : arguments) {
            result += (Integer) arg;
        }
        return result;
    }
}
