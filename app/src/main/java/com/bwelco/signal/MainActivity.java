package com.bwelco.signal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bwelco.signal.SignalPackage.ThreadMode;
import com.example.GetMsg;

@GetMsg(id = 2, name = "MAIN")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SignalReceiver(threadMode = ThreadMode.MAINTHREAD)
    public void onEvent1(MainActivity activity) {

    }
}
