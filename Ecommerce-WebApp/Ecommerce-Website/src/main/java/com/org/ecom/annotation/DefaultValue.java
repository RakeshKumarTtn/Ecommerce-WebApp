package com.org.ecom.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
     Source code recreated from a .class file by IntelliJ IDEA
     (powered by FernFlower decompiler)
*/

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface DefaultValue {
    boolean staticConstructor() default false;

    int staticIntConstructor() default 0;
}

