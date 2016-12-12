package com.bwelco.signal;

import android.os.Bundle;

import com.bwelco.signal.SignalPackage.Signal;

public class ExtendMainActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_main);

        Signal.getDefault().regist(this);
    }

    @SignalReceiver
    public void MyEvent(ExtendMainActivity activity){

    }
}
