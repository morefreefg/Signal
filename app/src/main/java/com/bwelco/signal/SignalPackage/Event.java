package com.bwelco.signal.SignalPackage;

/**
 * Created by bwelco on 2016/12/14.
 */

public class Event {
    Class<?> targetClass;
    String targetMethod;
    Object[] params;

    public Event(Class<?> targetClass, String targetMethod, Object[] params) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.params = params;
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
}
