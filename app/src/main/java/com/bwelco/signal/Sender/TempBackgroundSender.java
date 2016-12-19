package com.bwelco.signal.Sender;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.bwelco.signal.SignalPackage.Event;
import com.bwelco.signal.SignalPackage.PendingEvent;
import com.bwelco.signal.SignalPackage.RegisterInfo;
import com.bwelco.signal.SignalPackage.Signal;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by bwelco on 2016/12/19.
 */

public class TempBackgroundSender extends HandlerThread implements EventHandler {

    BackgroundHandler handler;
    Signal signal;
    PriorityBlockingQueue<PendingEvent> queue;

    public TempBackgroundSender(String name, Signal signal) {
        super(name);
        handler = new BackgroundHandler();
        this.signal = signal;
        queue = new PriorityBlockingQueue<PendingEvent>();
    }

    @Override
    public void handleEvent(Event event, RegisterInfo registerInfo) {
        PendingEvent pendingEvent = PendingEvent.obtainPendingPost(event, registerInfo);

        synchronized (this) {
            queue.put(pendingEvent);

            Message message = Message.obtain();
            long delayMillis = pendingEvent.event.getDelayMillis();

            try {
                message.obj = queue.take();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            handler.sendMessageDelayed(message, delayMillis);
        }
    }

    class BackgroundHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            synchronized (this) {
                PendingEvent pendingEvent = (PendingEvent) msg.obj;
                signal.invokeRegister(pendingEvent.registerInfo, pendingEvent.event.getParams());
                PendingEvent.releasePendingEvent(pendingEvent);
            }
        }
    }
}
