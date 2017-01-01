package com.bwelco.signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bwelco on 2016/12/14.
 */

public class SendingThreadState {
    final List<Event> eventQueue = new ArrayList<Event>();
    boolean isSending;
    boolean isMainThread;
    boolean delay;
}
