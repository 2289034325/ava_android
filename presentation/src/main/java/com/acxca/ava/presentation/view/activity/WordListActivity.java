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
import android.widget.ImageView;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.consts.Lang;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.DaggerDictionaryComponent;
import com.acxca.ava.presentation.di.components.DaggerReadingComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.di.components.ReadingComponent;
import com.acxca.ava.presentation.view.fragment.ReadingFragment;
import com.acxca.ava.presentation.view.fragment.TranslateDialogFragment;
import com.acxca.ava.presentation.view.fragment.WordListFragment;
import com.acxca.domain.BookMark;
import com.acxca.domain.Word;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that shows details of a certain user.
 */
public class WordListActivity extends BaseActivity implements HasComponent<DictionaryComponent> {

    private static final String INTENT_EXTRA_PARAM = "INTENT_EXTRA_PARAM";
    private static final String INSTANCE_STATE_PARAM = "INSTANCE_STATE_PARAM";

    private int lang;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.iv_back)
    ImageView iv_back;

    public static Intent getCallingIntent(Context context, int lang) {
        Intent callingIntent = new Intent(context, WordListActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM, lang);
        return callingIntent;
    }

    private WordListFragment wordListFragment;

    private DictionaryComponent dictionaryComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_wordlist);
        ButterKnife.bind(this);

        this.initializeActivity(savedInstanceState);
        this.initializeInjector();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putInt(INSTANCE_STATE_PARAM, lang);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            this.lang = getIntent().getIntExtra(INTENT_EXTRA_PARAM, 1);
            wordListFragment = WordListFragment.getInstance(lang);
            addFragment(R.id.container, wordListFragment);
        } else {
            this.lang = savedInstanceState.getInt(INSTANCE_STATE_PARAM);
        }

        tv_title.setText(Lang.fromId(lang).getName());
    }

    private void initializeInjector() {
        this.dictionaryComponent = DaggerDictionaryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public DictionaryComponent getComponent() {
        return dictionaryComponent;
    }

    @OnClick(R.id.iv_back)
    void naviBack() {
        finish();
    }
}
