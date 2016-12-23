package com.bwelco.signal;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bwelco on 2016/12/23.
 */

public class TypeCheck {

    private static Map<Class<?>, Class<?>> revertMap = new HashMap<Class<?>, Class<?>>();

    static {
        revertMap.put(int.class, java.lang.Integer.class);
        revertMap.put(float.class, java.lang.Float.class);
        revertMap.put(long.class, java.lang.Long.class);
        revertMap.put(double.class, java.lang.Double.class);
        revertMap.put(byte.class, java.lang.Byte.class);
        revertMap.put(short.class, java.lang.Short.class);
        revertMap.put(boolean.class, java.lang.Boolean.class);
        revertMap.put(char.class, java.lang.Character.class);
    }

//    // a 是 注册的， b是发送的
//    public static boolean isParamMatch(Class<?>[] a, Class<?>[] b) {
//        if (a.length != b.length) return false;
//        int length = a.length;
//        for (int i = 0;i < length;i++) {
//            Class<?> aClass = a[i];
//            Class<?> bClass = b[i];
//
//            if (aClass.isPrimitive()) aClass = revertMap.get(aClass);
//            if (bClass.isPrimitive()) bClass = revertMap.get(bClass);
//
//
//        }
//    }
}
