package com.bwelco.signal;

import java.util.List;

/**
 * Created by bwelco on 2016/12/22.
 */

public interface GetRegisterInfoInterface {
    List<com.bwelco.signal.RegisterMethodInfo> getRegisterByClass(Class<?> clazz);
}
