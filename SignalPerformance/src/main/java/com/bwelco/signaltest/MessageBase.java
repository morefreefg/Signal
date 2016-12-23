package com.bwelco.signaltest;

/**
 * Created by bwelco on 2016/12/23.
 */

public class MessageBase implements GetMessage{
    public String message;

    public MessageBase(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
