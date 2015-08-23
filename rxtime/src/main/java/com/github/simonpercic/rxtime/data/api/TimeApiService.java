package com.github.simonpercic.rxtime.data.api;

import com.github.simonpercic.rxtime.data.api.model.response.TimeResponse;

import retrofit.http.GET;
import rx.Observable;

/**
 * Time API Retrofit Service.
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
public interface TimeApiService {

    /**
     * Get current Utc time.
     *
     * @return Deserialized Time API Response
     */
    @GET("/utc/now") Observable<TimeResponse> getUtcNow();
}
