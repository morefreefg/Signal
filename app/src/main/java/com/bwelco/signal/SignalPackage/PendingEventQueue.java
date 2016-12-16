package com.bwelco.signal.SignalPackage;

/**
 * Created by bwelco on 2016/12/16.
 */

public class PendingEventQueue {
    PendingEvent head;
    PendingEvent tail;

    synchronized void enqueue(PendingEvent pendingEvent) {
        if (pendingEvent == null)
            throw new NullPointerException("pendingEvent is null, can't be enqueued");

        if (tail != null) {
            tail.next = pendingEvent;
            tail = pendingEvent;
        } else if (head == null) {
            head = tail = pendingEvent;
        } else {
            throw new IllegalStateException("Head present, but no tail");
        }

        notifyAll();
    }



}
