package com.bwelco.signal.SignalPackage;

import com.bwelco.signal.MethodFinder.IndexMethodInfo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bwelco on 2016/12/7.
 */

public class SignalRegistMethodIndex {

    public static Map<Class<?>, RegisterMethodInfo> map = null;

    static {
        map = new HashMap<Class<?>, RegisterMethodInfo>();

        map.put(com.bwelco.signal.MethodFinder.MethodFinderIndex.class, putIndex(new IndexMethodInfo(
                "", ThreadMode.ASYNC, com.bwelco.signal.MethodFinder.MethodFinderReflex.class,
                java.beans.beancontext.BeanContext.class
        )));
    }

    private static RegisterMethodInfo putIndex(IndexMethodInfo indexMethodInfo){
        RegisterMethodInfo registerMethodInfo = null;
        try {
            Method method = indexMethodInfo.getTargetClass().getDeclaredMethod(
                    indexMethodInfo.getMethodName(), indexMethodInfo.getParams()
            );
            registerMethodInfo = new RegisterMethodInfo(indexMethodInfo.getMethodName(),
                    indexMethodInfo.getThreadMode(), method, indexMethodInfo.getParams());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return registerMethodInfo;
    }
}
