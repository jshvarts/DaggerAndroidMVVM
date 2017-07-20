package com.example.daggerandroidmvvm.common;

import javax.inject.Inject;

import io.reactivex.Single;

public class LoadCommonGreetingUseCase {
    private final CommonGreetingRepository greetingRepository;

    @Inject
    public LoadCommonGreetingUseCase(CommonGreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    public Single<String> execute() {
        return greetingRepository.getGreeting();
    }
}
