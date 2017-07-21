package com.example.daggerandroidmvvm.lobby;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daggerandroidmvvm.R;

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

        observeGreetingData();
    }

    @OnClick(R.id.common_greeting_button)
    void onCommonGreetingButtonClicked() {
        greetingTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);
        viewModel.loadCommonGreeting();
    }

    @OnClick(R.id.lobby_greeting_button)
    void onLobbyGreetingButtonClicked() {
        greetingTextView.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);
        viewModel.loadLobbyGreeting();
    }

    private void observeGreetingData() {
        viewModel.getGreetingText().observe(this, newValue -> displayGreeting(newValue));
        viewModel.getGreetingError().observe(this, error -> displayGreetingError(error));
    }

    private void displayGreeting(String greeting) {
        loadingIndicator.setVisibility(View.GONE);
        greetingTextView.setVisibility(View.VISIBLE);
        greetingTextView.setText(greeting);
    }

    private void displayGreetingError(Throwable throwable) {
        loadingIndicator.setVisibility(View.GONE);
        Timber.e(throwable.getMessage());
        Toast.makeText(this, R.string.greeting_error, Toast.LENGTH_SHORT).show();
    }
}
