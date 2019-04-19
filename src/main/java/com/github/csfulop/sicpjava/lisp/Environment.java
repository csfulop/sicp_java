package com.github.csfulop.sicpjava.lisp;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> env = new HashMap<>();

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
            throw new LispException("Unbound variable " + key + "!");
        }
        return value;
    }
}
