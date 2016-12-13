package com.bwelco.signal.SignalPackage;

import com.bwelco.signal.MethodFinder.MethodFinderReflex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bwelco on 2016/12/7.
 */

public class Signal {

    static volatile Signal defaultInstance;
    private static final Map<Class<?>, List<RegisterMethodInfo>> METHOD_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, RegisterInfo> REGISTERS = new HashMap<>();

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
            // 注册函数信息
            subscribe(target, methodInfo);
        }

    }


    public void subscribe(Object target, List<RegisterMethodInfo> methodInfos){

        for (RegisterMethodInfo methodInfo : methodInfos) {
            RegisterInfo newRegister = new RegisterInfo(target, methodInfo);
            // key 是 class 和 methodname 组成的
            String key = newRegister.getTarget().getClass().getName() + newRegister.getMethodInfo().methodName;

            if (REGISTERS.containsKey(key)) {
                EventLogger.i("target " + target.getClass().getSimpleName() + " has already registed " +
                        newRegister.getMethodInfo().methodName);
                return;
            } else {
                REGISTERS.put(key, newRegister);
            }

        }
    }

    public void unRegist(Object target){
        List<RegisterMethodInfo> methodInfos = findRegistedMethod(target.getClass());

        for (RegisterMethodInfo methodInfo : methodInfos) {
            RegisterInfo newRegister = new RegisterInfo(target, methodInfo);
            String key = newRegister.getTarget().getClass().getName() + newRegister.getMethodInfo().methodName;
            if (REGISTERS.containsKey(key)) {
                REGISTERS.remove(key);
            } else {
                EventLogger.i("target " + target.getClass().getSimpleName() + " has already registed " +
                        newRegister.getMethodInfo().methodName);
                return;
            }
        }


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
