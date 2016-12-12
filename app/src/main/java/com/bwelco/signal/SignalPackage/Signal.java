package com.bwelco.signal.SignalPackage;

import android.util.Log;

/**
 * Created by bwelco on 2016/12/7.
 */

public class Signal {

    static volatile Signal defaultInstance;

    public static Signal getDefault() {
        if (defaultInstance == null) {
            synchronized (Signal.class) {
                if (defaultInstance == null) {
                    defaultInstance = new Signal();
                }
            }
        }
        return defaultInstance;
    }

    public void regist(Object target){
        Class targetClass = target.getClass();
        RegisterMethodInfo methodInfo = null;
        if ((methodInfo = MethodFinder.find(targetClass)) != null) {
            Log.i("admin", "class name = " + methodInfo.methodName);
        } else {
            Log.i("admin", "method not find");
        }
    }

    public void registByBroadCast(Object target){
        Class targetClass = target.getClass();
        RegisterMethodInfo methodInfo = null;
        if ((methodInfo = MethodFinder.find(targetClass)) != null) {
            Log.i("admin", "class name = " + methodInfo.methodName);
        } else {
            Log.i("admin", "method not find");
        }
    }

    private Signal() {

    }

    public void send(Object... args) {

    }
}
