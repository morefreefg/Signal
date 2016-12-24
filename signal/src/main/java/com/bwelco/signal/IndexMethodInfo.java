package com.bwelco.signal;


import com.bwelco.signal.processer.ThreadMode;

/**
 * Created by bwelco on 2016/12/22.
 */

public class IndexMethodInfo {
    String methodName;
    ThreadMode threadMode;
    Class<?> targetClass;
    Class<?>[] params;

    public IndexMethodInfo(String methodName, ThreadMode threadMode, Class<?> targetClass, Class<?>... params) {
        this.methodName = methodName;
        this.threadMode = threadMode;
        this.targetClass = targetClass;
        this.params = params;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public void setParams(Class<?>[] params) {
        this.params = params;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class<?> targetClass) {
        this.targetClass = targetClass;
    }
}
