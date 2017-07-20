package com.example.daggerandroidmvvm.lobby;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.example.daggerandroidmvvm.common.LoadCommonGreetingUseCase;
import com.example.daggerandroidmvvm.mvp.BasePresenter;
import com.example.daggerandroidmvvm.rx.SchedulersFacade;

import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

class LobbyPresenter extends BasePresenter<LobbyGreetingContract.LobbyView>
        implements LobbyGreetingContract.LobbyPresenter {

    private final LoadCommonGreetingUseCase loadCommonGreetingUseCase;

    private final LoadLobbyGreetingUseCase loadLobbyGreetingUseCase;

    private final SchedulersFacade schedulersFacade;

    private final BehaviorRelay<RequestState> requestStateObserver
            = BehaviorRelay.createDefault(RequestState.IDLE);

    LobbyPresenter(LobbyGreetingContract.LobbyView view,
                   LoadCommonGreetingUseCase loadCommonGreetingUseCase,
                   LoadLobbyGreetingUseCase loadLobbyGreetingUseCase,
                   SchedulersFacade schedulersFacade) {
        super(view);
        this.loadCommonGreetingUseCase = loadCommonGreetingUseCase;
        this.loadLobbyGreetingUseCase = loadLobbyGreetingUseCase;
        this.schedulersFacade = schedulersFacade;

        observeRequestState();
    }

    @Override
    public void loadCommonGreeting() {
        loadGreeting(loadCommonGreetingUseCase.execute());
    }

    @Override
    public void loadLobbyGreeting() {
        loadGreeting(loadLobbyGreetingUseCase.execute());
    }

    private void loadGreeting(Single<String> greetingSingle) {
        addDisposable(greetingSingle
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.ui())
                .doOnSubscribe(s -> publishRequestState(RequestState.LOADING))
                .doOnSuccess(s -> publishRequestState(RequestState.COMPLETE))
                .doOnError(t -> publishRequestState(RequestState.ERROR))
                .subscribe(view::displayGreeting, view::displayGreetingError)
        );
    }

    private void publishRequestState(RequestState requestState) {
        addDisposable(Observable.just(requestState)
                .observeOn(schedulersFacade.ui())
                .subscribe(requestStateObserver));
    }

    private void observeRequestState() {
        requestStateObserver.subscribe(requestState -> {
            switch (requestState) {
                case IDLE:
                    break;
                case LOADING:
                    view.hideGreeting();
                    view.setLoadingIndicator(true);
                    break;
                case COMPLETE:
                    view.setLoadingIndicator(false);
                    break;
                case ERROR:
                    view.setLoadingIndicator(false);
                    break;
            }
        }, Timber::e);
    }
}
