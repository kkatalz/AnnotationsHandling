package com.jintin.autofactory;

public @interface ExampleAnnotation {
    String value() default "some_default_value";
    String nameOfValue();
    int[] arrayOfInt();
}
