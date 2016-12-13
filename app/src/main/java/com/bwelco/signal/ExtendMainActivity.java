package com.bwelco.signal;

import android.os.Bundle;
import android.view.View;

import com.bwelco.signal.SignalPackage.EventLogger;
import com.bwelco.signal.SignalPackage.Signal;
import com.bwelco.signal.SignalPackage.ThreadMode;

public class ExtendMainActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_main);

        findViewById(R.id.dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventLogger.i("clazz name = " + this.getClass().getSimpleName());
                Signal.getDefault().send(ExtendMainActivity.class, "onEvent1", ExtendMainActivity.this);
            }
        });
    }

    @SignalReceiver(threadMode = ThreadMode.POSTERTHREAD)
    public void MyEvent(ExtendMainActivity activity, int i){

    }


    @Override
    protected void onResume() {
        Signal.getDefault().regist(this);
        EventLogger.i("onresume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Signal.getDefault().unRegist(this);
        EventLogger.i("onpause");
        super.onPause();
    }

}
