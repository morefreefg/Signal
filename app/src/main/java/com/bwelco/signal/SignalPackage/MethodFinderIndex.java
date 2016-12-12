package com.bwelco.signal.SignalPackage;

/**
 * Created by bwelco on 2016/12/7.
 */

public class MethodFinderIndex {

    // Get method By annotation 通过编译时注解获取方法
    public static RegisterMethodInfo find(Class<?> clazz){
        return SignalRegistMethodIndex.map.get(clazz);
    }
}
