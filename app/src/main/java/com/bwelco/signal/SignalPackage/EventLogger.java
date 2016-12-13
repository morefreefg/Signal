package com.bwelco.signal.SignalPackage;

import android.util.Log;

/**
 * Created by bwelco on 2016/12/12.
 */

public class EventLogger {

    public static void i(String str) {
        Log.i("admin", str);
    }
    public static void e(String str) {
        Log.e("admin", str);
    }
}
