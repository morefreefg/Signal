package com.bwelco.signal;

import android.os.Bundle;
import android.view.View;

import com.bwelco.signal.SignalPackage.EventLogger;
import com.bwelco.signal.SignalPackage.Signal;
import com.bwelco.signal.SignalPackage.ThreadMode;

import java.util.Random;

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
                        while (true)
                            Signal.getDefault().send(ExtendMainActivity.class, "MyEvent",
                                    "from thread " + Thread.currentThread().getName(), new Random().nextInt());
                    }
                });

                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true)
                            Signal.getDefault().send(ExtendMainActivity.class, "MyEvent",
                                    "from thread " + Thread.currentThread().getName(), new Random().nextInt());
                    }
                });

                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true)
                            Signal.getDefault().send(ExtendMainActivity.class, "MyEvent",
                                    "from thread " + Thread.currentThread().getName(), new Random().nextInt());
                    }
                });

                Thread thread4 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true)
                            Signal.getDefault().send(ExtendMainActivity.class, "MyEvent",
                                    "from thread " + Thread.currentThread().getName(), new Random().nextInt());
                    }
                });


                thread.start();
                thread2.start();
                thread3.start();
                thread4.start();

            }

        });
    }

    @SignalReceiver(threadMode = ThreadMode.ASYNC)
    public void MyEvent(String s, int i) {
        EventLogger.i(s + "i = " + i);
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
