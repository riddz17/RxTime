package com.github.simonpercic.rxtime.di;

import com.github.simonpercic.rxtime.data.api.TimeApiService;
import com.github.simonpercic.rxtime.data.api.model.response.TimeResponse;
import com.github.simonpercic.rxtime.data.api.typeadapter.TimeResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by Simon Percic on 16/08/15.
 */
@Module
public class LibraryModule {

    @Provides Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(TimeResponse.class, new TimeResponseDeserializer())
                .create();
    }

    @Provides Converter provideConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint("http://www.timeapi.org/");
    }

    @Provides RequestInterceptor provideRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };
    }

    @Provides RestAdapter provideRestAdapter(Endpoint endpoint, RequestInterceptor requestInterceptor,
            Converter converter) {

        return new Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(converter)
                .build();
    }

    @Provides TimeApiService provideTimeApiService(RestAdapter restAdapter) {
        return restAdapter.create(TimeApiService.class);
    }
}
