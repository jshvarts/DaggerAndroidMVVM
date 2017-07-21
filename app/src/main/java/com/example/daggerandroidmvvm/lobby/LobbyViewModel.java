package com.example.daggerandroidmvvm.lobby;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.daggerandroidmvvm.common.LoadCommonGreetingUseCase;
import com.example.daggerandroidmvvm.rx.SchedulersFacade;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

class LobbyViewModel extends ViewModel {

    private final LoadCommonGreetingUseCase loadCommonGreetingUseCase;

    private final LoadLobbyGreetingUseCase loadLobbyGreetingUseCase;

    private final SchedulersFacade schedulersFacade;

    private final MutableLiveData<String> greetingText = new MutableLiveData<>();

    private final MutableLiveData<Throwable> greetingError = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    LobbyViewModel(LoadCommonGreetingUseCase loadCommonGreetingUseCase,
                          LoadLobbyGreetingUseCase loadLobbyGreetingUseCase,
                          SchedulersFacade schedulersFacade) {
        this.loadCommonGreetingUseCase = loadCommonGreetingUseCase;
        this.loadLobbyGreetingUseCase = loadLobbyGreetingUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    void loadCommonGreeting() {
        loadGreeting(loadCommonGreetingUseCase.execute());
    }

    void loadLobbyGreeting() {
        loadGreeting(loadLobbyGreetingUseCase.execute());
    }

    MutableLiveData<String> getGreetingText() {
        return greetingText;
    }

    MutableLiveData<Throwable> getGreetingError() {
        return greetingError;
    }

    /**
     * Clears disposables, releases resources, etc.
     */
    void cleanup() {
        disposables.clear();
    }

    private void loadGreeting(Single<String> single) {
        disposables.add(single
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .subscribe(
                        greeting -> greetingText.setValue(greeting),
                        error -> greetingError.setValue(error)
                )
        );
    }
}
