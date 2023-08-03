package com.example.demo.config;

import java.lang.annotation.*;

@Target({ ElementType.FIELD, ElementType.TYPE })
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface DataLengh {
    int min() default 0;

    int max() default 2147483647;

    boolean isRequired() default false;

    String message() default "字段长度不符合";
}
