package com.github.csfulop.sicpjava.lisp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestEnvironment {

    private Environment env;

    @BeforeEach
    void setUp() {
        env = new Environment();
    }

    @Test
    void defineAndGetVariable() {
        // given
        // when
        env.define("asdf", 123);
        // then
        assertThat(env.get("asdf").isPresent(), is(true));
        assertThat(env.get("asdf").get(), is(123));
    }

    @Test
    void defineAndSetAndGetVariable() {
        // given
        // when
        env.define("asdf", 123);
        env.set("asdf", 234);
        // then
        assertThat(env.get("asdf").isPresent(), is(true));
        assertThat(env.get("asdf").get(), is(234));
    }

    @Test
    void setWithoutDefine() {
        // given
        // when
        LispException exception = assertThrows(LispException.class, () -> env.set("asdf", 234));
        // then
        assertThat(exception.getMessage(),is("Variable asdf is not defined!"));
    }

    @Test
    void overDefineExistingVariable() {
        // given
        // when
        env.define("asdf", 123);
        env.define("asdf", 234);
        // then
        assertThat(env.get("asdf").isPresent(), is(true));
        assertThat(env.get("asdf").get(), is(234));
    }

    @Test
    void keyNotInEnvironment() {
        // given
        // when
        Optional<Object> result = env.get("asdf");
        //then
        assertThat(env.get("asdf").isPresent(), is(false));
    }
}
