package com.example.daggerandroidmvvm.lobby;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.daggerandroidmvvm.common.domain.interactors.LoadCommonGreetingUseCase;
import com.example.daggerandroidmvvm.rx.SchedulersFacade;

class LobbyViewModelFactory implements ViewModelProvider.Factory {

    private final LoadCommonGreetingUseCase loadCommonGreetingUseCase;

    private final LoadLobbyGreetingUseCase loadLobbyGreetingUseCase;

    private final SchedulersFacade schedulersFacade;

    LobbyViewModelFactory(LoadCommonGreetingUseCase loadCommonGreetingUseCase,
                          LoadLobbyGreetingUseCase loadLobbyGreetingUseCase,
                          SchedulersFacade schedulersFacade) {
        this.loadCommonGreetingUseCase = loadCommonGreetingUseCase;
        this.loadLobbyGreetingUseCase = loadLobbyGreetingUseCase;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LobbyViewModel.class)) {
            return (T) new LobbyViewModel(loadCommonGreetingUseCase, loadLobbyGreetingUseCase, schedulersFacade);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
