/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.view.adapter.CardStackAdapter;
import com.acxca.ava.presentation.view.adapter.WordViewPageAdapter;
import com.acxca.domain.Word;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Activity that shows details of a certain user.
 */
public class WordTestActivity extends BaseActivity {

    private static final String INTENT_EXTRA_PARAM = "INTENT_EXTRA_PARAM";
    private static final String INSTANCE_STATE_PARAM = "INSTANCE_STATE_PARAM";

    private List<Word> wordList;
    private int currentIndex;

    @Bind(R.id.tv_title)
    TextView tv_title;

    public static Intent getCallingIntent(Context context, List<Word> wordList) {
        Intent callingIntent = new Intent(context, WordTestActivity.class);
        // tricky !!!
        callingIntent.putExtra(INTENT_EXTRA_PARAM, (Serializable) wordList);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_wordtest);
        ButterKnife.bind(this);

        this.initializeActivity(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putSerializable(INSTANCE_STATE_PARAM, (Serializable) wordList);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * Initializes this activity.
     */
    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            wordList = (List<Word>) getIntent().getSerializableExtra(INTENT_EXTRA_PARAM);
        } else {
            wordList = (List<Word>) savedInstanceState.getSerializable(INSTANCE_STATE_PARAM);
        }
        currentIndex = 0;

        tv_title.setText(String.format("%s/%s",currentIndex+1,wordList.size()));

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        cardStackView.setLayoutManager(new CardStackLayoutManager(this));

        cardStackView.setAdapter(new CardStackAdapter(this,wordList));

//        WordViewPageAdapter wvpd = new WordViewPageAdapter(wordList);
//        vp_pager.setAdapter(wvpd);
//        vp_pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
//            @Override
//            public void onPageSelected(int position) {
////                super.onPageSelected(position);
//                currentIndex = position;
//                tv_title.setText(String.format("%s/%s",currentIndex+1,wordList.size()));
//            }
//        });
    }

    @OnClick(R.id.iv_back)
    void naviBack() {
        finish();
    }
}
