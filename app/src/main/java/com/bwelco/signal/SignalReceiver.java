package com.bwelco.signal;

import com.bwelco.signal.SignalPackage.ThreadMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by bwelco on 2016/12/12.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SignalReceiver {
    ThreadMode threadMode() default ThreadMode.POSTERTHREAD;
}
