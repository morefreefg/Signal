package com.bwelco.signal.Sender;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bwelco.signal.SignalPackage.Event;
import com.bwelco.signal.SignalPackage.PendingEvent;
import com.bwelco.signal.SignalPackage.PendingEventQueue;
import com.bwelco.signal.SignalPackage.RegisterInfo;
import com.bwelco.signal.SignalPackage.Signal;

/**
 * Created by bwelco on 2016/12/14.
 */

public class MainThreadSender extends Handler {

    Looper looper;
    Signal signal;
    PendingEventQueue queue;

    public MainThreadSender(Signal signal, Looper looper) {
        super(looper);
        this.looper = looper;
        this.signal = signal;
        queue = new PendingEventQueue();
    }

    public void handleEvent(Event event, RegisterInfo registerInfo) {
        PendingEvent pendingEvent = PendingEvent.obtainPendingPost(event, registerInfo);
        synchronized (this) {
            queue.enqueue(pendingEvent);
            // 提醒
            sendMessage(obtainMessage());
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        PendingEvent pendingEvent = queue.poll();
        signal.invokeRegister(pendingEvent.registerInfo, pendingEvent.event.getParams());
    }
}
