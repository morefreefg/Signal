package com.bwelco.signal.SignalPackage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bwelco on 2016/12/7.
 */

public class Signal {

    static volatile Signal defaultInstance;
    private static final Map<Class<?>, List<RegisterMethodInfo>> METHOD_CACHE = new ConcurrentHashMap<>();

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
        Class<?> targetClass = target.getClass();
        // 反射获取注解方法
        List<RegisterMethodInfo> methodInfo = findRegistedMethod(targetClass);
        // 调试、打印方法
        printMethod(methodInfo);

        synchronized (this) {
            for (RegisterMethodInfo registerMethodInfo : methodInfo) {
                subscribe(target, methodInfo);
            }
        }

    }


    public void subscribe(Object target, List<RegisterMethodInfo> methodInfo){

    }

    public List<RegisterMethodInfo> findRegistedMethod(Class<?> targetClass){
        List<RegisterMethodInfo> ret = METHOD_CACHE.get(targetClass);
        if (ret == null) {
            ret = MethodFinderReflex.find(targetClass);
            // 放入缓存里面
            METHOD_CACHE.put(targetClass, ret);
        }

        return ret;
    }


    private void printMethod(List<RegisterMethodInfo> info){
        for (RegisterMethodInfo methodInfo : info) {
            EventLogger.i("\nmethod name : " + methodInfo.getMethodName());
            for (Class<?> param : methodInfo.getParams()) {
                EventLogger.i("      param : " + param.getName());
            }
            EventLogger.i("\n");
        }
    }


    private Signal() {

    }

    public void send(Object... args) {

    }
}
