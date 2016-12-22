package com.bwelco.signal.MethodFinder;

import com.bwelco.signal.SignalPackage.RegisterMethodInfo;

import java.util.List;

/**
 * Created by bwelco on 2016/12/22.
 */

public interface GetRegisterInfoInterface {
    List<RegisterMethodInfo> getRegisterByClass(Class<?> clazz);
}
