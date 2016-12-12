package com.bwelco.signal.SignalPackage;

/**
 * Created by bwelco on 2016/12/7.
 */

public class MethodFinder {

    public static RegisterMethodInfo find(Class<?> clazz){
        return SignalRegistMethodIndex.map.get(clazz);
    }
}
