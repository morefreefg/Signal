package com.bwelco.signal.Sender;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.bwelco.signal.SignalPackage.Signal;

/**
 * Created by bwelco on 2016/12/14.
 */

public class MainThreadSender extends Handler{

    Looper looper = Looper.getMainLooper();
    Signal signal;

    public MainThreadSender(Signal signal){
        this.signal = signal;
    }




    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
