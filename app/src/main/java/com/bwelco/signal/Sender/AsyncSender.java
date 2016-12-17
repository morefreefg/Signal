package com.bwelco.signal.Sender;

import com.bwelco.signal.SignalPackage.Event;
import com.bwelco.signal.SignalPackage.PendingEvent;
import com.bwelco.signal.SignalPackage.RegisterInfo;
import com.bwelco.signal.SignalPackage.Signal;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by bwelco on 2016/12/14.
 */

public class AsyncSender implements Runnable, EventHandler{

    Signal signal;
    LinkedBlockingQueue<PendingEvent> queue;
    public AsyncSender(Signal signal){
        this.signal = signal;
        queue = new LinkedBlockingQueue<PendingEvent>();
    }

    @Override
    public void handleEvent(Event event, RegisterInfo registerInfo) {
        PendingEvent pendingEvent = PendingEvent.obtainPendingPost(event, registerInfo);

        try {
            queue.put(pendingEvent);
        } catch (InterruptedException e) {
            throw new IllegalStateException("pending queue is full.");
        }

        signal.getExecutorService().submit(this);
    }

    @Override
    public void run() {

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
