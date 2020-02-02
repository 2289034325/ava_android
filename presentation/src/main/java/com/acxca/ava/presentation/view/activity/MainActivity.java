package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ActionMode;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.consts.Consts;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.DaggerDictionaryComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.view.component.TabView;
import com.acxca.ava.presentation.view.fragment.BookMarkListFragment;
import com.acxca.ava.presentation.view.fragment.ChooseCountDialogFragment;
import com.acxca.ava.presentation.view.fragment.SpeechListFragment;
import com.acxca.ava.presentation.view.fragment.WordStatListFragment;
import com.acxca.ava.presentation.view.fragment.WritingListFragment;
import com.acxca.domain.BookMark;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity implements HasComponent<DictionaryComponent>,
        WordStatListFragment.WordStatListListener,BookMarkListFragment.BookMarkListListener{

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }

    private DictionaryComponent dictionaryComponent;
    private FragmentManager fm;
    private Fragment mCurrentFragment;
    private WordStatListFragment wordStatListFragment;
    private BookMarkListFragment bookMarkListFragment;
    private SpeechListFragment speechListFragment;
    private WritingListFragment writingListFragment;

    @Bind(R.id.tv_vocab) TabView tvVocab;
    @Bind(R.id.tv_speech) TabView tvSpeech;
    @Bind(R.id.tv_writing) TabView tvWriting;
    @Bind(R.id.tv_bookmark) TabView tvBookmark;
    @Bind(R.id.tv_me) TabView tvMe;


    private ActionMode mActionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dictionaryComponent = DaggerDictionaryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();

        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences sp = getSharedPreferences(Consts.SP_NAME, 0);
        String token = sp.getString(Consts.SP_KEY_TOKEN, null);

        if (token == null) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        } else {
            AndroidApplication app = (AndroidApplication)this.getApplicationContext();
            app.sharedBag.put(Consts.SB_KEY_TOKEN,token);
            setContentView(R.layout.activity_main);
            findViewById(R.id.iv_back).setVisibility(View.GONE);
            ButterKnife.bind(this);

            fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();


            wordStatListFragment = new WordStatListFragment();
            bookMarkListFragment = new BookMarkListFragment();
            speechListFragment = new SpeechListFragment();
            writingListFragment = new WritingListFragment();

            mCurrentFragment = wordStatListFragment;
            ft.add(R.id.container, wordStatListFragment, "frg_voc").
                    add(R.id.container, speechListFragment, "frg_speech").hide(speechListFragment).
                    add(R.id.container, writingListFragment, "frg_writing").hide(writingListFragment).
                    add(R.id.container, bookMarkListFragment, "frg_bookmark").hide(bookMarkListFragment).
                    show(wordStatListFragment).commit();
        }
    }

    @Override
    public DictionaryComponent getComponent() {
        return dictionaryComponent;
    }

    @OnClick(R.id.tv_bookmark)
    void gotoBookMark() {
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(mCurrentFragment).show(bookMarkListFragment).commit();
        mCurrentFragment = bookMarkListFragment;

        TextView title = findViewById(R.id.tv_title);
        title.setText("书签");
    }

    @OnClick(R.id.tv_vocab)
    void gotoVocabulary() {

        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(mCurrentFragment).show(wordStatListFragment).commit();
        mCurrentFragment = wordStatListFragment;

        TextView title = findViewById(R.id.tv_title);
        title.setText("词汇");
    }

    @OnClick(R.id.tv_speech)
    void gotoSpeech() {

        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(mCurrentFragment).show(speechListFragment).commit();
        mCurrentFragment = speechListFragment;

        TextView title = findViewById(R.id.tv_title);
        title.setText("会话");
    }

    @OnClick(R.id.tv_writing)
    void gotoWriting() {

        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(mCurrentFragment).show(writingListFragment).commit();
        mCurrentFragment = writingListFragment;

        TextView title = findViewById(R.id.tv_title);
        title.setText("写作");
    }


    @Override
    public void onWordStatItemClicked() {

        View sheetView = getLayoutInflater().inflate(R.layout.dialog_bottom_wordstat, null);
        BottomSheetDialog bsd = new BottomSheetDialog(this);
        bsd.setContentView(sheetView);

        LinearLayout ll_scan = (LinearLayout) sheetView.findViewById(R.id.ll_scan);
        LinearLayout ll_learn = (LinearLayout) sheetView.findViewById(R.id.ll_learn);
        LinearLayout ll_revise = (LinearLayout) sheetView.findViewById(R.id.ll_revise);
        LinearLayout ll_cancel = (LinearLayout) sheetView.findViewById(R.id.ll_cancel);

        ll_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage("浏览");
            }
        });
        ll_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseCountDialogFragment fragment = new ChooseCountDialogFragment();
                fragment.init(MainActivity.this,getSupportFragmentManager(),"learn");
                fragment.show();
            }
        });
        ll_revise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage("复习");
            }
        });
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastMessage("取消");
            }
        });

        FrameLayout bottomSheet = bsd.findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackground(null);

        bsd.show();
    }

    @Override
    public void onBookMarkItemClicked(BookMark bookMark) {
        Context context = this;
        Intent intentToLaunch = ReadingActivity.getCallingIntent(context, bookMark);
        context.startActivity(intentToLaunch);
    }
}
