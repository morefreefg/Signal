package com.bwelco.signal.SignalPackage;

import android.os.Looper;

import com.bwelco.signal.MethodFinder.MethodFinderReflex;

import java.lang.reflect.InvocationTargetException;
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
    // ThreadLocal
    private final ThreadLocal<SendingThreadState> currentSendingThreadState = new ThreadLocal<SendingThreadState>() {
        @Override
        protected SendingThreadState initialValue() {
            return new SendingThreadState();
        }
    };

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

    public void regist(Object target) {
        Class<?> targetClass = target.getClass();
        // 反射获取注解方法
        List<RegisterMethodInfo> methodInfo = findRegistedMethod(targetClass);
        // 调试、打印方法
        // printMethod(methodInfo);

        synchronized (this) {
            // 注册函数信息
            subscribe(target, methodInfo);
        }

    }


    public void subscribe(Object target, List<RegisterMethodInfo> methodInfos) {

        for (RegisterMethodInfo methodInfo : methodInfos) {
            RegisterInfo newRegister = new RegisterInfo(target, methodInfo);
            // key 是 class 和 methodname 组成的
            String key = newRegister.getTarget().getClass().getName() + newRegister.getMethodInfo().methodName;

            if (REGISTERS.containsKey(key)) {
                EventLogger.e("target " + target.getClass().getSimpleName() + " has already registed " +
                        newRegister.getMethodInfo().methodName);
                return;
            } else {
                REGISTERS.put(key, newRegister);
            }

        }
    }

    public void unRegist(Object target) {
        List<RegisterMethodInfo> methodInfos = findRegistedMethod(target.getClass());

        for (RegisterMethodInfo methodInfo : methodInfos) {
            RegisterInfo newRegister = new RegisterInfo(target, methodInfo);
            String key = newRegister.getTarget().getClass().getName() + newRegister.getMethodInfo().methodName;
            if (REGISTERS.containsKey(key)) {
                REGISTERS.remove(key);
            } else {
                EventLogger.i("target " + target.getClass().getSimpleName() + " has not registed ");
                return;
            }
        }


    }

    public List<RegisterMethodInfo> findRegistedMethod(Class<?> targetClass) {
        List<RegisterMethodInfo> ret = METHOD_CACHE.get(targetClass);
        if (ret == null) {
            ret = MethodFinderReflex.find(targetClass);
            // 放入缓存里面
            METHOD_CACHE.put(targetClass, ret);
        }

        return ret;
    }


    private void printMethod(List<RegisterMethodInfo> info) {
        for (RegisterMethodInfo methodInfo : info) {
            EventLogger.i("\nmethod name = " + methodInfo.getMethodName() + ", threadmode = " + methodInfo.getThreadMode());

            int i = 0;
            for (Class<?> param : methodInfo.getParams()) {
                EventLogger.i("      param " + (++i) + " = " + param.getName());
            }
            EventLogger.i("\n");
        }
    }


    private Signal() {

    }

    public void send(Class<?> targetClass, String methodName, Object... args) {


        // 获取当前线程的SendingThreadState
        SendingThreadState currentThread = currentSendingThreadState.get();
        // 获取队列
        List<Event> eventQueue = currentThread.eventQueue;
        // 添加当前事件
        eventQueue.add(new Event(targetClass, methodName, args));

        // 正在发送事件
        if (!currentThread.isSending) {
            // 判断是否是主线程
            currentThread.isMainThread = Looper.getMainLooper().getThread() == Thread.currentThread();

            try {
                // 清空发送队列
                while (!eventQueue.isEmpty()) {
                    sendSingleEvent(eventQueue.remove(0), currentThread);
                }

            } finally {
                // 重置线程发送状态
                currentThread.isSending = false;
                currentThread.isMainThread = false;
            }

        }
    }

    void sendSingleEvent(Event event, SendingThreadState threadState) {

        String key = event.targetClass.getName() + event.getTargetMethod();
        Object[] args = event.getParams();
        EventLogger.i("arg.length() = " + args.length);

        if (REGISTERS.containsKey(key)) {

            if (REGISTERS.get(key).getMethodInfo().getParams().length != args.length) {
                EventLogger.i("length = " + REGISTERS.get(key).getMethodInfo().getParams().length);
                EventLogger.i(" param num not match!");
                return;
            }

            int index = 0;
            for (Class<?> paramType : REGISTERS.get(key).getMethodInfo().getParams()) {
                EventLogger.i("arg param = " + args[index].getClass().getName());
                EventLogger.i("regist param = " + paramType.getName());

                if (!args[index++].getClass().equals(paramType)) {
                    EventLogger.i(" param not match!");
                    return;
                }
            }

            EventLogger.i("get!");
            sendEventToRegister(REGISTERS.get(key), event, threadState);

        } else {
            EventLogger.i("can't get");
        }
    }

    void sendEventToRegister(RegisterInfo register, Event event, SendingThreadState threadState){
        switch (register.methodInfo.getThreadMode()) {

            case POSTERTHREAD:
                invokeRegister(register, event.getParams());
                break;

            case MAINTHREAD:
                // 主线程往主线程发
                if (threadState.isMainThread) {
                    invokeRegister(register, event.getParams());
                } else {
                    // 其他线程往主线程发

                }
                break;

            case BACKGROUND:

                break;

            case ASYNC:

                break;
        }
    }

    void invokeRegister(RegisterInfo registerInfo, Object... signal) {
        try {
            registerInfo.methodInfo.method.invoke(registerInfo.target, signal);
        } catch (InvocationTargetException e) {
            EventLogger.e("InvocationTargetException!");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }
}
