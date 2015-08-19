package com.github.simonpercic.rxtime.data.api;

import com.github.simonpercic.rxtime.data.api.model.response.TimeResponse;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by Simon Percic on 14/08/15.
 */
public interface TimeApiService {

    @GET("/utc/now") Observable<TimeResponse> getUtcNow();
}
