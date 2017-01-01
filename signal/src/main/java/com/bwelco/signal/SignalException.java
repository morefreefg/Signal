package com.bwelco.signal;

/**
 * Created by bwelco on 2016/12/23.
 */

public class SignalException extends RuntimeException {

    private static final long serialVersionUID = -2912559384646531479L;

    public SignalException(String detailMessage){
        super(detailMessage);
    }

    public SignalException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
