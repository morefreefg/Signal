package com.bwelco.signal.Sender;

import com.bwelco.signal.SignalPackage.Event;
import com.bwelco.signal.SignalPackage.RegisterInfo;

/**
 * Created by bwelco on 2016/12/17.
 */

public interface EventHandler {
    void handleEvent(Event event, RegisterInfo registerInfo);
}
