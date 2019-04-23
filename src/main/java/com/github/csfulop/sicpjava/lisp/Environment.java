package com.github.csfulop.sicpjava.lisp;

import com.github.csfulop.sicpjava.lisp.exceptions.LispException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> env = new HashMap<>();
    private Environment enclosing = null;

    public void define(String key, Object value) {
        env.put(key, value);
    }

    public void set(String key, Object value) {
        if (!env.containsKey(key)) {
            throw new LispException("Variable " + key + " is not defined!");
        }
        env.put(key, value);
    }

    public Object get(String key) {
        Object value = env.get(key);
        if (value == null) {
            if (enclosing == null) {
                throw new LispException("Unbound variable " + key + "!");
            } else {
                return enclosing.get(key);
            }
        }
        return value;
    }

    public Environment extend(String[] variables, Object[] values) {
        Environment extended = new Environment();
        extended.enclosing = this;
        if (variables.length < values.length) {
            throw new LispException("Too many values supplied: " + Arrays.toString(variables) + " -- " + Arrays.toString(values));
        }
        if (variables.length > values.length) {
            throw new LispException("Too many variables supplied: " + Arrays.toString(variables) + " -- " + Arrays.toString(values));
        }
        for (int i = 0; i < variables.length; i++) {
            extended.define(variables[i], values[i]);
        }
        return extended;
    }
}
