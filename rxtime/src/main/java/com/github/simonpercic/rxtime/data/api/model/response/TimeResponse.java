package com.github.simonpercic.rxtime.data.api.model.response;

/**
 * Created by Simon Percic on 14/08/15.
 */
public class TimeResponse {

    private final long timeMillis;

    public TimeResponse(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public long getTimeMillis() {
        return timeMillis;
    }
}
