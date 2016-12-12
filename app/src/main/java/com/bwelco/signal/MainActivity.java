package com.bwelco.signal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bwelco.signal.SignalPackage.Signal;
import com.example.GetMsg;

@GetMsg(id = 2, name = "MAIN")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Signal.getDefault().regist(this);
        Signal.getDefault().send(1);

    }

    public void event(int c) {
        Log.i("admin", "c = " + c);
    }
}
