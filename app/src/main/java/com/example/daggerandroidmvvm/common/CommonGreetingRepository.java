package com.example.daggerandroidmvvm.common;

import io.reactivex.Single;

public class CommonGreetingRepository {
    public Single<String> getGreeting() {
        return Single.just("Hello from CommonGreetingRepository");
    }
}
