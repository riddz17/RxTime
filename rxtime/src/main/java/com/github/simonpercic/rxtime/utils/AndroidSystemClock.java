package com.github.simonpercic.rxtime.utils;

import android.os.SystemClock;

/**
 * Created by Simon Percic on 19/08/15.
 */
public final class AndroidSystemClock {

    private AndroidSystemClock() {
    }

    public static long getDeviceUptimeMillis() {
        return SystemClock.elapsedRealtime();
    }
}
