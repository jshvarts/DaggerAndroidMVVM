package com.example.daggerandroidmvvm.common.domain.interactors;

import io.reactivex.Single;

public interface LoadGreetingUseCase {
    public Single<String> execute();
}
