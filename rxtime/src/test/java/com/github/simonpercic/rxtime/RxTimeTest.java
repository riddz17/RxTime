package com.github.simonpercic.rxtime;

import com.github.simonpercic.rxtime.utils.AndroidSystemClock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.Transformer;
import rx.Observer;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Simon Percic on 19/08/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AndroidSystemClock.class)
public class RxTimeTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockStatic(AndroidSystemClock.class);
    }

    @Test
    public void testCurrentTime() throws Exception {
        when(AndroidSystemClock.getDeviceUptimeMillis()).thenReturn(2000L);

        final long millis = 1440006565000L;

        RxTime rxTime = spy(new RxTime());
        when(rxTime.getNetworkUtcTimeObservable()).thenReturn(Observable.just(millis));

        // first call
        final CountDownLatch latch = new CountDownLatch(1);

        rxTime.currentTime().compose(schedulersTransformer).subscribe(new TestTimeObserver() {
            @Override public void onNext(Long time) {
                assertEquals(millis, time.longValue());
                latch.countDown();
            }
        });

        if (!latch.await(400, TimeUnit.MILLISECONDS)) {
            fail();
        }

        // cached value
        TestSubscriber<Long> testSubscriber = new TestSubscriber<>();
        rxTime.currentTime().compose(schedulersTransformer).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(millis);

        verify(rxTime, times(1)).getNetworkUtcTimeObservable();
    }

    @Test
    public void testCurrentTimeUptime() throws Exception {
        long startUptime = 2000L;
        long endUptime = 4000L;

        when(AndroidSystemClock.getDeviceUptimeMillis()).thenReturn(startUptime);

        final long millis = 1440006565000L;

        RxTime rxTime = spy(new RxTime());
        when(rxTime.getNetworkUtcTimeObservable()).thenReturn(Observable.just(millis));

        // first call
        final CountDownLatch latch = new CountDownLatch(1);

        rxTime.currentTime().compose(schedulersTransformer).subscribe(new TestTimeObserver() {
            @Override public void onNext(Long time) {
                assertEquals(millis, time.longValue());
                latch.countDown();
            }
        });

        if (!latch.await(400, TimeUnit.MILLISECONDS)) {
            fail();
        }

        // cached value
        when(AndroidSystemClock.getDeviceUptimeMillis()).thenReturn(endUptime);

        TestSubscriber<Long> testSubscriber = new TestSubscriber<>();
        rxTime.currentTime().compose(schedulersTransformer).subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue((millis - startUptime) + endUptime);
    }

    private static abstract class TestTimeObserver implements Observer<Long> {
        @Override public void onCompleted() {

        }

        @Override public void onError(Throwable e) {
            fail();
        }
    }

    private final Transformer<Long, Long> schedulersTransformer =
            new Transformer<Long, Long>() {
                @Override public Observable<Long> call(Observable<Long> observable) {
                    return observable.subscribeOn(Schedulers.immediate()).observeOn(Schedulers.immediate());
                }
            };
}
