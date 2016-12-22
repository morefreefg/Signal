package com.bwelco.signalperformance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bwelco.signal.SignalPackage.Signal;
import com.bwelco.signal.SignalPackage.ThreadMode;
import com.bwelco.signal.SignalReceiver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Signal.getDefault().regist(this);

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signal.getDefault().send(MainActivity.class, "get","test", 1);
            }
        });
    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void get(String s, int i){
        Log.i("admin", "get message " + s);
    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void get_private(float a){

    }
}
