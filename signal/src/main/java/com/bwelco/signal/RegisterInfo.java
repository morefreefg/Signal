package com.bwelco.signal;

/**
 * Created by bwelco on 2016/12/13.
 */

public class RegisterInfo {

    // 订阅者
    Object target;

    // 订阅者的函数信息
    RegisterMethodInfo methodInfo;

    public RegisterInfo(Object target, RegisterMethodInfo methodInfo) {
        this.target = target;
        this.methodInfo = methodInfo;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public RegisterMethodInfo getMethodInfo() {
        return methodInfo;
    }

    public void setMethodInfo(RegisterMethodInfo methodInfo) {
        this.methodInfo = methodInfo;
    }
}
