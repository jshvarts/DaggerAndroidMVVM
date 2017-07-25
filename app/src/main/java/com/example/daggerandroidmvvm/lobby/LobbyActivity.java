package com.example.daggerandroidmvvm.lobby;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daggerandroidmvvm.R;
import com.example.daggerandroidmvvm.common.viewmodel.Response;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class LobbyActivity extends LifecycleActivity {

    @Inject
    LobbyViewModelFactory viewModelFactory;

    @BindView(R.id.greeting_textview)
    TextView greetingTextView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    private LobbyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LobbyViewModel.class);

        observeLoadingStatus();

        observeResponse();
    }

    @OnClick(R.id.common_greeting_button)
    void onCommonGreetingButtonClicked() {
        viewModel.loadCommonGreeting();
    }

    @OnClick(R.id.lobby_greeting_button)
    void onLobbyGreetingButtonClicked() {
        viewModel.loadLobbyGreeting();
    }

    private void observeLoadingStatus() {
        viewModel.getLoadingStatus().observe(this, isLoading -> processLoadingStatus(isLoading));
    }

    private void observeResponse() {
        viewModel.getResponse().observe(this, response -> processResponse(response));
    }

    private void processLoadingStatus(boolean isLoading) {
        greetingTextView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        loadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void processResponse(Response<String> response) {
        switch (response.status) {
            case SUCCESS:
                greetingTextView.setText(response.data);
                break;

            case ERROR:
                Timber.e(response.error);
                Toast.makeText(this, R.string.greeting_error, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
