package com.github.simonpercic.rxtime.di;

import com.github.simonpercic.rxtime.RxTime;

import dagger.Component;

/**
 * Created by Simon Percic on 15/08/15.
 */
@Component(
        modules = LibraryModule.class
)
public interface LibraryComponent {
    void inject(RxTime rxTime);
}
