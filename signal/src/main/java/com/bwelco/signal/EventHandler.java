package com.bwelco.signal;

/**
 * Created by bwelco on 2016/12/17.
 */

public interface EventHandler {
    void handleEvent(Event event, com.bwelco.signal.RegisterInfo registerInfo);
}
