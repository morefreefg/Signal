package com.bwelco.signal;

import java.util.List;

/**
 * Created by bwelco on 2016/12/7.
 */

public class MethodFinderIndex {

    private static GetRegisterInfoInterface getRegisterInfoInterface;
    static boolean tryToFindSignalIndex = false;

    // Get method By annotation 通过编译时注解获取方法
    public static List<RegisterMethodInfo> find(Class<?> clazz) {
        if (getRegisterInfoInterface == null) {
            if (tryToFindSignalIndex) return null;
            else {
                try {
                    Class injectorClazz = Class.forName("com.bwelco.signalsperf.MySignalIndex");
                    getRegisterInfoInterface = (GetRegisterInfoInterface) injectorClazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    EventLogger.i("Signal index class not found");
                    tryToFindSignalIndex = true;
                    return null;
                }

                return getRegisterInfoInterface.getRegisterByClass(clazz);

            }
        } else {
            return getRegisterInfoInterface.getRegisterByClass(clazz);
        }
    }
}
