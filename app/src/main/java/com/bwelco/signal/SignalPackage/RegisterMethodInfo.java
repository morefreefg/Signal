package com.bwelco.signal.SignalPackage;

/**
 * Created by bwelco on 2016/12/7.
 */

public class RegisterMethodInfo {

    String methodName;
    Class<?>[] params;

    public RegisterMethodInfo(){}


    public RegisterMethodInfo(String methodName) {
        this.methodName = methodName;
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
