package com.example.daggerandroidmvvm.lobby;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.daggerandroidmvvm.common.domain.interactors.LoadCommonGreetingUseCase;
import com.example.daggerandroidmvvm.common.viewmodel.Response;
import com.example.daggerandroidmvvm.rx.SchedulersFacade;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

class LobbyViewModel extends ViewModel {

    private final LoadCommonGreetingUseCase loadCommonGreetingUseCase;

    private final LoadLobbyGreetingUseCase loadLobbyGreetingUseCase;

    private final SchedulersFacade schedulersFacade;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Response<String>> response = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingStatus = new MutableLiveData<>();

    LobbyViewModel(LoadCommonGreetingUseCase loadCommonGreetingUseCase,
                          LoadLobbyGreetingUseCase loadLobbyGreetingUseCase,
                          SchedulersFacade schedulersFacade) {
        this.loadCommonGreetingUseCase = loadCommonGreetingUseCase;
        this.loadLobbyGreetingUseCase = loadLobbyGreetingUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    void loadCommonGreeting() {
        loadGreeting(loadCommonGreetingUseCase.execute());
    }

    void loadLobbyGreeting() {
        loadGreeting(loadLobbyGreetingUseCase.execute());
    }

    MutableLiveData<Response<String>> getResponse() {
        return response;
    }

    MutableLiveData<Boolean> getLoadingStatus() {
        return loadingStatus;
    }

    private void loadGreeting(Single<String> single) {
        disposables.add(single
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> loadingStatus.setValue(true))
                .doAfterTerminate(() -> loadingStatus.setValue(false))
                .subscribe(
                        greeting -> response.setValue(Response.success(greeting)),
                        throwable -> response.setValue(Response.error(throwable))
                )
        );
    }
}
