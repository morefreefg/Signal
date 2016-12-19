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

                EventLogger.i("send");

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Signal.getDefault().sendDelayed(ExtendMainActivity.class, "MyEvent", 10,
                                    "from thread " + Thread.currentThread().getName(), 1);

                        Signal.getDefault().sendDelayed(ExtendMainActivity.class, "MyEvent", 6000,
                                "from thread " + Thread.currentThread().getName(), 2);

                        Signal.getDefault().sendDelayed(ExtendMainActivity.class, "MyEvent", 5000,
                                "from thread " + Thread.currentThread().getName(), 3);

                        Signal.getDefault().sendDelayed(ExtendMainActivity.class, "MyEvent", 3000,
                                "from thread " + Thread.currentThread().getName(), 4);
                    }
                });

                thread.start();

            }

        });
    }

    @SignalReceiver(threadMode = ThreadMode.BACKGROUND)
    public void MyEvent(String s, int i) throws InterruptedException {
        EventLogger.i("get message" + s + " i = " + i);
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
