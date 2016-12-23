package com.bwelco.signal;

import android.os.Message;

import java.util.Stack;

/**
 * Created by bwelco on 2016/12/19.
 */

public class MessagePool {


    static Message[] messages;
    static Stack<Integer> free_index;
    int poolsize;

    // arg1 表示 index

    public MessagePool(int poolsize) {
        this.poolsize = poolsize;
        messages = new Message[poolsize];
        free_index = new Stack<Integer>();

        for (int i = 0; i < messages.length; i++) {
            messages[i] = new Message();
            messages[i].arg1 = i;
            free_index.push(i);
        }
    }

    public static Message obtainMessage() {
        synchronized (messages) {
            if (free_index.size() > 0) {
                int index = free_index.pop();
                return messages[index];
            } else {
                return new Message();
            }
        }

    }

    public void releaseMessage(Message message) {
        synchronized (messages) {
            int index = message.arg1;
            free_index.push(index);
        }
    }
}
