package com.bwelco.signal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by bwelco on 2016/12/14.
 */

public class MainThreadSender extends Handler implements EventHandler {

    Looper looper;
    Signal signal;
    PriorityBlockingQueue<PendingEvent> queue;

    public MainThreadSender(Signal signal, Looper looper) {
        super(looper);
        this.looper = looper;
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

            sendMessageDelayed(message, delayMillis);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        synchronized (this) {
            PendingEvent pendingEvent = (PendingEvent) msg.obj;
            signal.invokeRegister(pendingEvent.registerInfo, pendingEvent.event.getParams());
            PendingEvent.releasePendingEvent(pendingEvent);
        }
    }

//    static class PendingEventList extends ArrayList<PendingEvent> {
//        public void addByTime(PendingEvent pendingEvent){
//            long time = pendingEvent.event.getAtTime();
//            for (int i = 0 ; i < size(); i++) {
//                if (time)
//            }
//        }
//    }


}
