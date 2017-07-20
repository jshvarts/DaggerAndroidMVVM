package com.example.daggerandroidmvvm.lobby;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class LobbyActivity extends AppCompatActivity implements LobbyGreetingContract.LobbyView {

    private static final String BUNDLE_DATA_KEY_GREETING = "greeting";

    @Inject
    LobbyPresenter presenter;

    @BindView(R.id.greeting_textview)
    TextView greetingTextView;

    @BindView(R.id.loading_indicator)
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby_activity);

        ButterKnife.bind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!TextUtils.isEmpty(greetingTextView.getText())) {
            outState.putCharSequence(BUNDLE_DATA_KEY_GREETING, greetingTextView.getText());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        greetingTextView.setText(savedInstanceState.getCharSequence(BUNDLE_DATA_KEY_GREETING));
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.stop();
    }

    @Override
    @OnClick(R.id.common_greeting_button)
    public void onCommonGreetingButtonClicked() {
        presenter.loadCommonGreeting();
    }

    @Override
    @OnClick(R.id.lobby_greeting_button)
    public void onLobbyGreetingButtonClicked() {
        presenter.loadLobbyGreeting();
    }

    @Override
    public void displayGreeting(String greeting) {
        greetingTextView.setVisibility(View.VISIBLE);
        greetingTextView.setText(greeting);
    }

    @Override
    public void hideGreeting() {
        greetingTextView.setVisibility(View.GONE);
    }

    @Override
    public void displayGreetingError(Throwable throwable) {
        Timber.e(throwable.getMessage());
        Toast.makeText(this, R.string.greeting_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        loadingIndicator.setVisibility(active ? View.VISIBLE : View.GONE);
    }
}
