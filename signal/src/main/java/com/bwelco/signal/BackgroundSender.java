package com.bwelco.signal;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by bwelco on 2016/12/14.
 */

public class BackgroundSender implements Runnable, EventHandler {

    Signal signal;
    LinkedBlockingQueue<PendingEvent> queue;

    public BackgroundSender(Signal signal) {
        this.signal = signal;
        queue = new LinkedBlockingQueue<PendingEvent>();
        signal.getExecutorService().submit(this);
    }

    @Override
    public void handleEvent(Event event, RegisterInfo registerInfo) {
        synchronized (this) {
            PendingEvent pendingEvent = PendingEvent.obtainPendingPost(event, registerInfo);

            try {
                queue.put(pendingEvent);
            } catch (InterruptedException e) {
                throw new IllegalStateException("pending queue is full.");
            }
        }
    }

    @Override
    public void run() {

        while (true) {
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
