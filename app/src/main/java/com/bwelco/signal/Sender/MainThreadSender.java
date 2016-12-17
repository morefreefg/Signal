package com.bwelco.signal.Sender;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bwelco.signal.SignalPackage.Event;
import com.bwelco.signal.SignalPackage.PendingEvent;
import com.bwelco.signal.SignalPackage.RegisterInfo;
import com.bwelco.signal.SignalPackage.Signal;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by bwelco on 2016/12/14.
 */

public class MainThreadSender extends Handler implements EventHandler {

    Looper looper;
    Signal signal;
    LinkedBlockingQueue<PendingEvent> queue;

    public MainThreadSender(Signal signal, Looper looper) {
        super(looper);
        this.looper = looper;
        this.signal = signal;
        queue = new LinkedBlockingQueue<PendingEvent>();
    }

    @Override
    public void handleEvent(Event event, RegisterInfo registerInfo) {
        PendingEvent pendingEvent = PendingEvent.obtainPendingPost(event, registerInfo);
        synchronized (this) {
            try {
                queue.put(pendingEvent);
            } catch (InterruptedException e) {
                throw new IllegalStateException("pending queue is full.");
            }
            // 提醒
            sendMessage(obtainMessage());
            notifyAll();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        synchronized (this) {

            PendingEvent pendingEvent = null;
            try {
                pendingEvent = queue.take();
            } catch (InterruptedException e) {
                throw new IllegalStateException("can't get pending event from LinkedBlockingQueue.");
            }

            signal.invokeRegister(pendingEvent.registerInfo, pendingEvent.event.getParams());
            PendingEvent.releasePendingEvent(pendingEvent);
        }
    }

}
