package com.github.simonpercic.observabletime.di;

import com.github.simonpercic.observabletime.ObservableTime;

import dagger.Component;

/**
 * Created by Simon Percic on 15/08/15.
 */
@Component(
        modules = LibraryModule.class
)
public interface LibraryComponent {
    void inject(ObservableTime observableTime);
}
