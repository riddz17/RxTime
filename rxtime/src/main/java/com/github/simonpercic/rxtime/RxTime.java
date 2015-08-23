package com.github.simonpercic.rxtime;

import com.github.simonpercic.rxtime.data.api.TimeApiService;
import com.github.simonpercic.rxtime.data.api.model.response.TimeResponse;
import com.github.simonpercic.rxtime.di.DaggerLibraryComponent;
import com.github.simonpercic.rxtime.di.LibraryModule;
import com.github.simonpercic.rxtime.utils.AndroidSystemClock;

import javax.inject.Inject;

import dagger.Lazy;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * RxTime.
 * <p/>
 * Useful for getting current UTC time independently of device timezone and time.
 * <p/>
 * On first use, UTC time is fetched from API at http://www.timeapi.org/utc/now and stored as a base value.
 * On further requests, UTC time is calculated from the base value and device uptime, to save on network calls.
 * <p/>
 * As a result, the UTC time is always correct, independently of the timezone set on device, even if the timezone
 * (or time) is changed on the device after (or before) RxTime is used.
 * <p/>
 * Use as a singleton in your app (pro-tip: use Dagger).
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
public class RxTime {

    // Time API Service lazy dagger instance
    @Inject Lazy<TimeApiService> timeApiService;

    // Base UTC time in millis.
    // Once this is set, time is calculated using this base value and device uptime.
    private long baseUtcTime = -1L;

    public RxTime() {
        DaggerLibraryComponent.builder()
                .libraryModule(new LibraryModule())
                .build()
                .inject(this);
    }

    /**
     * Get current UTC time in millis, independent of device timezone and time.
     *
     * @return current UTC time in millis.
     */
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

    /**
     * Get device uptime in millis.
     *
     * @return device uptime in millis.
     */
    private static long getDeviceUptimeMillis() {
        return AndroidSystemClock.getDeviceUptimeMillis();
    }

    /**
     * Get current UTC time from API.
     *
     * @return current UTC time in millis.
     */
    Observable<Long> getNetworkUtcTimeObservable() {
        return timeApiService.get().getUtcNow().map(new Func1<TimeResponse, Long>() {
            @Override public Long call(TimeResponse timeResponse) {
                return timeResponse.getTimeMillis();
            }
        });
    }
}
