# RxTime 

A very simple reactive Android library to obtain the current UTC time from the internet.

[![Build Status](https://api.travis-ci.org/simonpercic/RxTime.svg?branch=master)](https://travis-ci.org/simonpercic/RxTime)

### What does it do?
It fetches the current UTC time from [www.timeapi.org](http://www.timeapi.org/) using Retrofit.

### Why is it useful?
Useful for getting current UTC time independently of device timezone and the set time.

### How does it work?
On first use, UTC time is fetched from the API at [www.timeapi.org/utc/now](http://www.timeapi.org/utc/now) and stored as a base value. 

On further requests, UTC time is calculated from the base value and device uptime, to save on network calls.

As a result, the UTC time is always correct, independently of the timezone set on device, even if the timezone (or time) is changed on the device after (or before) RxTime is used.

## Usage

Add using Gradle:
```groovy
compile 'TODO'
```

Create a **singleton** instance of RxTime and use it:
```java
// a singleton somewhere
RxTime rxTime = new RxTime();

...

rxTime.currentTime()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(new Action1<Long>() {
        @Override public void call(Long time) {
            // use time
        }
    });
```

Use the awesome [Gradle Retrolambda Plugin](https://github.com/evant/gradle-retrolambda) with Java 8 to use lambdas:
```java
rxTime.currentTime()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(time -> { // use time });
```

### Singleton Pro-tip
Use [Dagger](https://github.com/google/dagger) to hold a single instance of RxTime.

## Scheduler
The Observable returned by RxTime runs on a background thread by default (using ```.subscribeOn(Schedulers.io())```).

## Sample application
See the included sample application to see a practical example of usage.

## License

Open source, distributed under the MIT License. See [LICENSE](LICENSE) for details.
