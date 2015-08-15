package com.github.simonpercic.observabletime;

import android.os.SystemClock;

import com.github.simonpercic.observabletime.data.api.TimeApiService;
import com.github.simonpercic.observabletime.data.api.model.response.TimeResponse;
import com.github.simonpercic.observabletime.data.api.typeadapter.TimeResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Simon Percic on 14/08/15.
 */
public class ObservableTime {

    private long baseUtcTime = -1L;

    public Observable<Long> currentTime() {
        if (baseUtcTime > 0) {
            return Observable.just(baseUtcTime + getDeviceUptimeMillis());
        }

        return getNetworkUtcTimeObservable().map(new Func1<Long, Long>() {
            @Override public Long call(Long time) {
                baseUtcTime = time - getDeviceUptimeMillis();
                return time;
            }
        }).subscribeOn(Schedulers.io());
    }

    private static long getDeviceUptimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    private Observable<Long> getNetworkUtcTimeObservable() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(TimeResponse.class, new TimeResponseDeserializer())
                .create();

        GsonConverter gsonConverter = new GsonConverter(gson);

        Endpoint endpoint = Endpoints.newFixedEndpoint("http://www.timeapi.org/");

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(gsonConverter)
                .build();

        TimeApiService timeApiService = restAdapter.create(TimeApiService.class);

        return timeApiService.getUtcNow().map(new Func1<TimeResponse, Long>() {
            @Override public Long call(TimeResponse timeResponse) {
                return timeResponse.getTimeMillis();
            }
        });
    }
}
