package com.github.simonpercic.rxtime.utils;

import android.os.SystemClock;

/**
 * Android system clock.
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
public final class AndroidSystemClock {

    private AndroidSystemClock() {
    }

    /**
     * Returns device uptime in milliseconds.
     *
     * @return elapsed milliseconds since boot.
     */
    public static long getDeviceUptimeMillis() {
        return SystemClock.elapsedRealtime();
    }
}
