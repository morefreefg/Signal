package com.bwelco.signal;

/**
 * Created by bwelco on 2016/12/23.
 */

public class SubScriber {

    Class<?> targetClass;
    String methodName;

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public SubScriber(Class<?> targetClass, String methodName) {
        this.targetClass = targetClass;
        this.methodName = methodName;
    }
}
