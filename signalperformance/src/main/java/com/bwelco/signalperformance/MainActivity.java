package com.bwelco.signalperformance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bwelco.signal.Signal;
import com.bwelco.signal.SubScriber;
import com.bwelco.signal.processer.SignalReceiver;
import com.bwelco.signal.processer.ThreadMode;
import com.bwelco.signaltest.MessageBase;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Signal.getDefault().unSubScribe(this);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signal.getDefault().send(new SubScriber(MainActivity.class, "onSignal"),
                        "message", "message2");

                Signal.getDefault().sendDelayed(new SubScriber(MainActivity.class, "onSignal"), 1000,
                        "message", "message2");
            }
        });
    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void onSignal(String s1, String s2) {

    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void get_private(MessageBase a) {

    }

}
