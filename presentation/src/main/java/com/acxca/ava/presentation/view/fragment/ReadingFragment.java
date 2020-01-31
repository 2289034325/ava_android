/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.consts.Consts;
import com.acxca.ava.presentation.consts.Lang;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.ApplicationComponent;
import com.acxca.ava.presentation.di.components.DaggerDictionaryComponent;
import com.acxca.ava.presentation.di.components.DaggerReadingComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.di.components.ReadingComponent;
import com.acxca.ava.presentation.di.modules.ActivityModule;
import com.acxca.ava.presentation.presenter.ReadingPresenter;
import com.acxca.ava.presentation.presenter.WordStatListPresenter;
import com.acxca.ava.presentation.view.ReadingView;
import com.acxca.ava.presentation.view.WordStatListView;
import com.acxca.ava.presentation.view.adapter.UserWordStatListAdapter;
import com.acxca.ava.presentation.view.adapter.UsersLayoutManager;
import com.acxca.domain.UserWordStat;
import com.acxca.domain.Word;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows a list of Users.
 */
public class ReadingFragment extends BaseFragment implements ReadingView, HasComponent<ReadingComponent> {
    private static final String PARAM_URL = "param_url";

    private Lang lang;

    public interface ServiceResultListener {
        void onSentenceResult(String meaning);

        void onWordResult(Word w);
    }

    private ServiceResultListener serviceResultListener;

    @Inject
    ReadingPresenter readingPresenter;

    private ReadingComponent readingComponent;

    @Bind(R.id.rl_progress)
    RelativeLayout rl_progress;
    @Bind(R.id.txb_url)
    EditText txbUrl;
    @Bind(R.id.wb_main)
    WebView webView;

    @Bind(R.id.iv_lang)
    ImageView iv_lang;

    public ReadingFragment() {
        setRetainInstance(true);
    }

    public static ReadingFragment getInstance(String url) {
        final ReadingFragment readingFragment = new ReadingFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(PARAM_URL, url);
        readingFragment.setArguments(arguments);
        return readingFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof ReadingFragment.ServiceResultListener) {
            this.serviceResultListener = (ReadingFragment.ServiceResultListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationComponent appc = ((AndroidApplication) getActivity().getApplication()).getApplicationComponent();

        this.readingComponent = DaggerReadingComponent.builder()
                .applicationComponent(appc)
                .activityModule(new ActivityModule(getActivity()))
                .build();

        this.readingComponent.inject(this);

        SharedPreferences sp = getActivity().getSharedPreferences(Consts.SP_NAME, 0);
        int intLang = sp.getInt(Consts.SP_LANG, Lang.EN.getId());
        this.lang = Lang.fromId(intLang);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_reading, container, false);
        ButterKnife.bind(this, fragmentView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String url = bundle.getString(PARAM_URL);
            txbUrl.setText(url);
            webView.loadUrl(url);
        }

        setLangFlg(lang);
//    setupRecyclerView();
        return fragmentView;
    }

    @OnClick(R.id.ib_back)
    void onBackClick() {
        webView.goBack();
    }

    @OnClick(R.id.ib_forward)
    void onForwardClick() {
        webView.goForward();
    }

    @OnClick(R.id.ib_refresh)
    void onRefreshClick() {
        webView.reload();
    }

    @OnClick(R.id.iv_lang)
    void onLangClick() {
        Lang lang = this.lang.next();

        SharedPreferences sp = getActivity().getSharedPreferences(Consts.SP_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Consts.SP_LANG, lang.getId());
        editor.apply();

        this.lang = lang;
        setLangFlg(lang);
    }

    private void setLangFlg(Lang lang){
//        Resources resources = getResources();
        switch (lang) {
            case EN:
                iv_lang.setImageResource(R.drawable.flg_en);
//                iv_lang.setImageDrawable(resources.getDrawable(R.drawable.flg_en));
//                iv_lang.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.flg_en));
                break;
            case JP:
                iv_lang.setImageResource(R.drawable.flg_jp);
                break;
            case KR:
                iv_lang.setImageResource(R.drawable.flg_kr);
                break;
            case FR:
                iv_lang.setImageResource(R.drawable.flg_fr);
                break;
            default:
                iv_lang.setImageResource(R.drawable.flg_en);
                break;

        }
    }


    public void onContextualMenuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mni_ava:
                // do some stuff
                break;
            case R.id.mni_ggl:
                // do some different stuff
                break;
            default:
                // ...
                break;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//    WebSettings settings = webView.getSettings();
//    String userAgentString = settings.getUserAgentString();
//    webView.loadUrl("https://xw.qq.com/?f=qqcom");
        this.readingPresenter.setView(this);
//    if (savedInstanceState == null) {
//      this.readingPresenter.loadUserWordStatList();
//    }
    }

    @OnClick(R.id.btn_ok)
    void onOKClick() {
        String url = txbUrl.getText().toString();
        webView.loadUrl(url);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.readingPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.readingPresenter.pause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.readingPresenter.destroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.serviceResultListener = null;
    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {
//    this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
//    this.rl_retry.setVisibility(View.GONE);
    }


    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return this.getActivity().getApplicationContext();
    }

    @Override
    public ReadingComponent getComponent() {
        return null;
    }

    @Override
    public void showTranslateWord(Word w) {
        this.serviceResultListener.onWordResult(w);
    }

    @Override
    public void showTranslateSentence(String meaning) {

    }

    public void startSearchWord(int lang, String word) {
        this.readingPresenter.searchWord(lang, word);
    }
}
