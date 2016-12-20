package com.bwelco.signal.SignalPackage;

import java.lang.reflect.Method;

/**
 * Created by bwelco on 2016/12/7.
 */

public class RegisterMethodInfo {

    String methodName;
    ThreadMode threadMode;
    Class<?>[] params;
    Method method;

    public RegisterMethodInfo() {
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
