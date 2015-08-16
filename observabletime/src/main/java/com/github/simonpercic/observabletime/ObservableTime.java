package com.github.simonpercic.observabletime;

import android.os.SystemClock;

import com.github.simonpercic.observabletime.data.api.TimeApiService;
import com.github.simonpercic.observabletime.data.api.model.response.TimeResponse;
import com.github.simonpercic.observabletime.di.DaggerLibraryComponent;
import com.github.simonpercic.observabletime.di.LibraryModule;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Simon Percic on 14/08/15.
 */
public class ObservableTime {

    @Inject Lazy<TimeApiService> timeApiService;

    private long baseUtcTime = -1L;

    public ObservableTime() {
        DaggerLibraryComponent.builder()
                .libraryModule(new LibraryModule())
                .build()
                .inject(this);
    }

    public Observable<Long> currentTime() {
        return Observable.defer(new Func0<Observable<Long>>() {
            @Override public Observable<Long> call() {
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
        });
    }

    private static long getDeviceUptimeMillis() {
        return SystemClock.elapsedRealtime();
    }

    private Observable<Long> getNetworkUtcTimeObservable() {
        return timeApiService.get().getUtcNow().map(new Func1<TimeResponse, Long>() {
            @Override public Long call(TimeResponse timeResponse) {
                return timeResponse.getTimeMillis();
            }
        });
    }
}
