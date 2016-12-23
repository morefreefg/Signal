package com.bwelco.signalperformance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bwelco.signal.Signal;
import com.bwelco.signal.SignalReceiver;
import com.bwelco.signal.SubScriber;
import com.bwelco.signal.ThreadMode;
import com.bwelco.signaltest.GetMessage;
import com.bwelco.signaltest.MessageSonTwo;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Signal.getDefault().regist(this);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signal.getDefault().send(new SubScriber(MainActivity.class, "get_private"),
                         new MessageSonTwo("admin"));
            }
        });
    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void get(String s, GetMessage getMessage) {
        Log.i("admin", "get message " + s);
        Log.i("admin", "get base message = " + getMessage.getMessage());
    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void get_private(float a) {

    }
}
