package com.bwelco.signal.SignalPackage;

/**
 * Created by bwelco on 2016/12/16.
 */

public class PendingEventQueue {
    PendingEvent head;
    PendingEvent tail;

    public synchronized void enqueue(PendingEvent pendingEvent) {
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

    public synchronized PendingEvent poll() {
        PendingEvent pendingEvent = head;
        if (head != null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        }
        return pendingEvent;
    }

    public synchronized PendingEvent poll(int maxMillisToWait) throws InterruptedException {
        if (head == null) {
            wait(maxMillisToWait);
        }
        return poll();
    }

}
