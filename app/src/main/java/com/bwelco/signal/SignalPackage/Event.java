package com.bwelco.signal.SignalPackage;

import android.os.SystemClock;

/**
 * Created by bwelco on 2016/12/14.
 */

public class Event {
    Class<?> targetClass;
    String targetMethod;
    Object[] params;
    long delayMillis;
    long atTime;

    public Event(Class<?> targetClass, String targetMethod, Object[] params, long delayMillis) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.params = params;
        this.delayMillis = delayMillis;
        this.atTime = SystemClock.uptimeMillis();
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public long getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public long getAtTime() {
        return atTime;
    }

    public void setAtTime(long atTime) {
        this.atTime = atTime;
    }
}
