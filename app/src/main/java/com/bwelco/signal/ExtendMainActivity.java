package com.bwelco.signal;

import android.os.Bundle;
import android.os.Looper;
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EventLogger.i("send thread :" + Thread.currentThread().getName() +
                                " id = " + Thread.currentThread().getId());
                        Signal.getDefault().send(ExtendMainActivity.class, "MyEvent", "my first signal message", 2);
                    }
                });
                thread.start();

            }

        });
    }

    @SignalReceiver(threadMode = ThreadMode.BACKGROUND)
    public void MyEvent(String s, int i) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            EventLogger.i("main thread");
        } else {
            EventLogger.i("thread :" + Thread.currentThread().getName() + " id = " + Thread.currentThread().getId());
        }

        EventLogger.i("get message success!\nmessage = " + s + "\n" + "recv message2 = " + i);
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
