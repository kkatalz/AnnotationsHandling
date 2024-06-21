package com.jintin.autofactory;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE})
public @interface AutoElement {
    String value();
}
