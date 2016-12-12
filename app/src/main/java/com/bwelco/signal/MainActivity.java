package com.bwelco.signal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.GetMsg;

@GetMsg(id = 2, name = "MAIN")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @SignalReceiver
    public void onEvent(int i, String k){

    }


    @SignalReceiver
    public void onEvent1(MainActivity activity) {

    }
}
