package com.bwelco.signal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by bwelco on 2016/12/12.
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface SignalReceiver {
    ThreadMode threadMode() default ThreadMode.POSTERTHREAD;
}
