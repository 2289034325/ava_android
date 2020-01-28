package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.consts.Consts;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.DaggerDictionaryComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.view.component.TabView;
import com.acxca.ava.presentation.view.fragment.ChooseCountDialogFragment;
import com.acxca.ava.presentation.view.fragment.ReadingFragment;
import com.acxca.ava.presentation.view.fragment.TranslateDialogFragment;
import com.acxca.ava.presentation.view.fragment.WordStatListFragment;
import com.acxca.domain.Word;

import android.support.design.widget.BottomSheetDialog;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity implements HasComponent<DictionaryComponent>,
        WordStatListFragment.WordStatListListener,ReadingFragment.ServiceResultListener{

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }

    private DictionaryComponent dictionaryComponent;
    private FragmentManager fm;
    private Fragment mCurrentFragment;
    private WordStatListFragment wordStatListFragment;
    private ReadingFragment readingFragment;

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
            readingFragment = new ReadingFragment();

            mCurrentFragment = wordStatListFragment;
            ft.add(R.id.container, wordStatListFragment, "frg_voc").add(R.id.container, readingFragment, "frg_reading").hide(readingFragment).show(wordStatListFragment).commit();
//            addFragment(R.id.container, new WordStatListFragment());
        }
    }

    @Override
    public DictionaryComponent getComponent() {
        return dictionaryComponent;
    }

    @OnClick(R.id.tv_bookmark)
    void gotoBookMark() {

        // 隐藏actionbar和tab
        findViewById(R.id.action_bar_container).setVisibility(View.GONE);
        findViewById(R.id.cl_tab).setVisibility(View.GONE);

        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(mCurrentFragment).show(readingFragment).commit();

        mCurrentFragment = readingFragment;
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mActionMode == null) {
            mActionMode = mode;
//            Menu menu = mode.getMenu();
//            // Remove the default menu items (select all, copy, paste, search)
//            menu.clear();
//
//            // If you want to keep any of the defaults,
//            // remove the items you don't want individually:
//            // menu.removeItem(android.R.id.[id_of_item_to_remove])
//
//            // Inflate your own menu items
//            mode.getMenuInflater().inflate(R.menu.menu_reading, menu);

            MenuInflater menuInflater = mode.getMenuInflater();
            Menu menu = mode.getMenu();

            menu.clear();
            menuInflater.inflate(R.menu.menu_reading, menu);

            menu.findItem(R.id.mni_ava).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    readingFragment.startSearchWord(1,"duo");
//                    TranslateDialogFragment fragment = new TranslateDialogFragment();
//                    fragment.init(MainActivity.this,getSupportFragmentManager(),"reading");
//                    fragment.show();

                    return true;
                }
            });
            menu.findItem(R.id.mni_ggl).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
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
    public void onSentenceResult(String meaning) {
        TranslateDialogFragment fragment = new TranslateDialogFragment();
        fragment.init(MainActivity.this,getSupportFragmentManager(),"read");
        fragment.show();
    }

    @Override
    public void onWordResult(Word w) {
        TranslateDialogFragment fragment = new TranslateDialogFragment();
                    fragment.init(MainActivity.this,getSupportFragmentManager(),"reading");
                    fragment.setWord(w);

                    fragment.show();
    }
}
