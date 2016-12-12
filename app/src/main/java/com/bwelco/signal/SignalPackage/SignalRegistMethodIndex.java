package com.bwelco.signal.SignalPackage;

import com.bwelco.signal.MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bwelco on 2016/12/7.
 */

public class SignalRegistMethodIndex {

    static final Map<Class<?>, RegisterMethodInfo> map;
    static {
        map = new HashMap<Class<?>, RegisterMethodInfo>();
        map.put(MainActivity.class, new RegisterMethodInfo("event"));
    }
}
