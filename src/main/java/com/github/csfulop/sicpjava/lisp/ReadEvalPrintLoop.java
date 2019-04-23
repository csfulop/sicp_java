package com.github.csfulop.sicpjava.lisp;

import com.github.csfulop.sicpjava.lisp.exceptions.EOFException;
import com.github.csfulop.sicpjava.lisp.exceptions.LispException;
import com.github.csfulop.sicpjava.lisp.functions.Add;
import com.github.csfulop.sicpjava.lisp.functions.Equals;
import com.github.csfulop.sicpjava.lisp.functions.Mul;
import com.github.csfulop.sicpjava.lisp.functions.Sub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadEvalPrintLoop {
    private Environment global = new Environment();
    private Parser parser = new Parser();
    private LispEvaluator evaluator = new LispEvaluator();
    private BufferedReader reader;

    public ReadEvalPrintLoop() {
        setUpBuiltInFunctions();
    }

    public static void main(String[] args) {
        ReadEvalPrintLoop repl = new ReadEvalPrintLoop();
        repl.readEvalPrintLoop();
    }

    private void setUpBuiltInFunctions() {
        global.define("+", new Add());
        global.define("-", new Sub());
        global.define("*", new Mul());
        global.define("=", new Equals());
    }

    protected String read() {
        System.out.print("> ");
        try {
            String line = reader.readLine();
            if (line == null) {
                throw new EOFException();
            }
            return line;
        } catch (IOException e) {
            throw new LispException(e);
        }
    }

    protected Object eval(String command) {
        return evaluator.eval(parser.parse(command), global);
    }

    protected void print(Object result) {
        System.out.println(result);
    }

    public Object evalPrint(String command) {
        Object result = eval(command);
        print(result);
        return result;
    }

    protected Object readEvalPrint() {
        return evalPrint(read());
    }

    public Object readEvalPrintLoop(InputStream in) {
        reader = new BufferedReader(new InputStreamReader(in));
        Object lastResult = null;
        try {
            while (true) {
                lastResult = readEvalPrint();
            }
        } catch (EOFException e) {
        }
        return lastResult;
    }

    public Object readEvalPrintLoop() {
        return readEvalPrintLoop(System.in);
    }
}
