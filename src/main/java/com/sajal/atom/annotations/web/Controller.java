package com.sajal.atom.annotations.web;

import com.sajal.atom.annotations.atom.Atom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Atom
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String path() default "";
}