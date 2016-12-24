package com.bwelco.signal;


import com.bwelco.signal.processer.SignalReceiver;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bwelco on 2016/12/12.
 */

public class MethodFinderReflex {

    private static final int BRIDGE = 0x40;
    private static final int SYNTHETIC = 0x1000;
    private static final int MODIFIERS_IGNORE = Modifier.ABSTRACT | Modifier.STATIC | BRIDGE | SYNTHETIC;


    public static List<RegisterMethodInfo> find(Class<?> clazz) {

        List<RegisterMethodInfo> ret = new ArrayList<RegisterMethodInfo>();

        while (clazz != null) {
            findByClass(clazz, ret);

            // 循环获取父类方法并加入
            clazz = clazz.getSuperclass();
            String clazzName = clazz.getName();
            /** Skip system classes, this just degrades performance. */
            if (clazzName.startsWith("java.") || clazzName.startsWith("javax.") || clazzName.startsWith("android.")) {
                clazz = null;
            }
        }

        return ret;
    }

    // 通过反射获取方法
    private static void findByClass(Class<?> clazz, List<RegisterMethodInfo> ret) {

        Method[] methods;

        methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            RegisterMethodInfo registerMethodInfo = new RegisterMethodInfo();
            int modifiers = method.getModifiers();

            // 是public方法 并且不是 抽象、静态等方法
            if ((modifiers & Modifier.PUBLIC) != 0 && (modifiers & MODIFIERS_IGNORE) == 0) {
                // 获取参数类型
                Class<?>[] parameterTypes = method.getParameterTypes();

                for (int i = 0; i < parameterTypes.length; i++) {
                    // 手动装箱
                    Class<?> paramClass = parameterTypes[i];
                    if (paramClass.equals(int.class)) {
                        parameterTypes[i] = Integer.class;
                    } else if (paramClass.equals(float.class)) {
                        parameterTypes[i] = Float.class;
                    } else if (paramClass.equals(boolean.class)) {
                        parameterTypes[i] = Boolean.class;
                    } else if (paramClass.equals(char.class)) {
                        parameterTypes[i] = Character.class;
                    } else if (paramClass.equals(double.class)) {
                        parameterTypes[i] = Double.class;
                    } else if (paramClass.equals(long.class)) {
                        parameterTypes[i] = Long.class;
                    } else if (paramClass.equals(short.class)) {
                        parameterTypes[i] = Short.class;
                    } else if (paramClass.equals(byte.class)) {
                        parameterTypes[i] = Byte.class;
                    }
                }

                // 获取注解方法
                SignalReceiver signalAnnotation = method.getAnnotation(SignalReceiver.class);

                if (signalAnnotation != null) {
                    // 加入方法名和参数名,运行线程模式
                    registerMethodInfo.setMethodName(method.getName());
                    registerMethodInfo.setParams(parameterTypes);
                    registerMethodInfo.setMethod(method);
                    registerMethodInfo.setThreadMode(signalAnnotation.threadMode());

                    ret.add(registerMethodInfo);
                }
            }
        }
    }
}
