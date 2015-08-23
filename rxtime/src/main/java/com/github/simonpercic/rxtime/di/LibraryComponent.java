package com.github.simonpercic.rxtime.di;

import com.github.simonpercic.rxtime.RxTime;

import dagger.Component;

/**
 * Dagger library component.
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
@Component(
        modules = LibraryModule.class
)
public interface LibraryComponent {
    void inject(RxTime rxTime);
}
