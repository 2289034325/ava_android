/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.DaggerReadingComponent;
import com.acxca.ava.presentation.di.components.DaggerUserComponent;
import com.acxca.ava.presentation.di.components.ReadingComponent;
import com.acxca.ava.presentation.di.components.UserComponent;
import com.acxca.ava.presentation.view.fragment.ReadingFragment;
import com.acxca.ava.presentation.view.fragment.TranslateDialogFragment;
import com.acxca.domain.Word;

/**
 * Activity that shows details of a certain user.
 */
public class ReadingActivity extends BaseActivity implements HasComponent<ReadingComponent>, ReadingFragment.ServiceResultListener {

    private static final String INTENT_EXTRA_PARAM_URL = "INTENT_EXTRA_PARAM_URL";
    private static final String INSTANCE_STATE_PARAM_URL = "INSTANCE_STATE_PARAM_URL";

    public static Intent getCallingIntent(Context context, String url) {
        Intent callingIntent = new Intent(context, ReadingActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_URL, url);
        return callingIntent;
    }

    private String url;
    private ReadingComponent readingComponent;
    private ReadingFragment readingFragment;

    private ActionMode mActionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_layout);

        this.initializeActivity(savedInstanceState);
        this.initializeInjector();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INSTANCE_STATE_PARAM_URL, this.url);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.url = getIntent().getStringExtra(INTENT_EXTRA_PARAM_URL);
            readingFragment = ReadingFragment.getInstance(url);
            addFragment(R.id.fragmentContainer, readingFragment);
        } else {
            this.url = savedInstanceState.getString(INSTANCE_STATE_PARAM_URL);
        }
    }

    private void initializeInjector() {
        this.readingComponent = DaggerReadingComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public ReadingComponent getComponent() {
        return readingComponent;
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mActionMode == null) {
            mActionMode = mode;

            MenuInflater menuInflater = mode.getMenuInflater();
            Menu menu = mode.getMenu();

            menu.clear();
            menuInflater.inflate(R.menu.menu_reading, menu);

            menu.findItem(R.id.mni_ava).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    readingFragment.startSearchWord(1, "duo");
                    return true;
                }
            });
            menu.findItem(R.id.mni_ggl).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    showToastMessage("ggl");
                    return true;
                }
            });
        }

        super.onActionModeStarted(mode);
    }


    @Override
    public void onActionModeFinished(ActionMode mode) {
        mActionMode = null;
        super.onActionModeFinished(mode);
    }


    @Override
    public void onSentenceResult(String meaning) {
        TranslateDialogFragment fragment = new TranslateDialogFragment();
        fragment.init(ReadingActivity.this, getSupportFragmentManager(), "read");
        fragment.show();
    }

    @Override
    public void onWordResult(Word w) {
        TranslateDialogFragment fragment = new TranslateDialogFragment();
        fragment.init(ReadingActivity.this, getSupportFragmentManager(), "reading");
        fragment.setWord(w);

        fragment.show();
    }
}
