package com.github.simonpercic.rxtime.data.api.model.response;

/**
 * Time API Response.
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
public class TimeResponse {

    // time in milliseconds
    private final long timeMillis;

    public TimeResponse(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public long getTimeMillis() {
        return timeMillis;
    }
}
