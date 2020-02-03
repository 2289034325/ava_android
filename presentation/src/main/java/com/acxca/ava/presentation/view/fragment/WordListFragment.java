/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.ApplicationComponent;
import com.acxca.ava.presentation.di.components.DaggerDictionaryComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.di.modules.ActivityModule;
import com.acxca.ava.presentation.presenter.WordListPresenter;
import com.acxca.ava.presentation.view.WordListView;
import com.acxca.ava.presentation.view.adapter.ListLayoutManager;
import com.acxca.ava.presentation.view.adapter.SpeechListAdapter;
import com.acxca.ava.presentation.view.adapter.WordListAdapter;
import com.acxca.domain.Speech;
import com.acxca.domain.Word;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Fragment that shows a list of Users.
 */
public class WordListFragment extends BaseFragment implements WordListView,HasComponent<DictionaryComponent> {
  private static final String PARAM = "param";

  @Override
  public DictionaryComponent getComponent() {
    return null;
  }

  @Inject
  WordListPresenter wordListPresenter;

  @Inject
  WordListAdapter listAdapter;

  private int lang;
  private int pageIndex;
  private int pageSize;

  private DictionaryComponent dictionaryComponent;

  @Bind(R.id.rv_list) RecyclerView rv_list;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.sl_wrapper) RefreshLayout sl_wrapper;

  public WordListFragment() {
    setRetainInstance(true);
    pageIndex = 0;
    pageSize = 20;
  }

  public static WordListFragment getInstance(int lang) {
    final WordListFragment wordListFragment = new WordListFragment();
    final Bundle arguments = new Bundle();
    arguments.putSerializable(PARAM, lang);
    wordListFragment.setArguments(arguments);
    return wordListFragment;
  }

  @Override
  public void renderWordList(List<Word> wordList) {
    if (wordList != null) {
      sl_wrapper.finishRefresh(1);//传入false表示刷新失败
      this.listAdapter.setItems(wordList);
      pageIndex = 0;
    }
  }

  @Override
  public void appendWordList(List<Word> wordList) {
    if (wordList != null) {
      sl_wrapper.finishLoadMore(1);
      this.listAdapter.appendItems(wordList);
      pageIndex++;
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ApplicationComponent appc = ((AndroidApplication) getActivity().getApplication()).getApplicationComponent();
    this.dictionaryComponent = DaggerDictionaryComponent.builder()
            .applicationComponent(appc)
            .activityModule(new ActivityModule(getActivity()))
            .build();

    this.dictionaryComponent.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_item_list_with_refresh, container, false);
    ButterKnife.bind(this, fragmentView);
    setupRecyclerView();

    sl_wrapper.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(RefreshLayout refreshlayout) {
        pageIndex = 0;
        wordListPresenter.loadUserWordList(lang,pageIndex,pageSize);
      }
    });
    sl_wrapper.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override
      public void onLoadMore(RefreshLayout refreshlayout) {
        wordListPresenter.loadUserWordList(lang,pageIndex+1,pageSize);
      }
    });

    Bundle bundle = this.getArguments();
    if (bundle != null) {
      int lang = bundle.getInt(PARAM);
      this.lang = lang;
    }

    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.wordListPresenter.setView(this);
    this.wordListPresenter.loadUserWordList(lang,pageIndex,pageSize);
  }

  @Override public void onResume() {
    super.onResume();
    this.wordListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.wordListPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    rv_list.setAdapter(null);
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.wordListPresenter.destroy();
  }

  @Override public void onDetach() {
    super.onDetach();
  }

  @Override public void showLoading() {
    this.rl_progress.setVisibility(View.VISIBLE);
    this.getActivity().setProgressBarIndeterminateVisibility(true);
  }

  @Override public void hideLoading() {
    this.rl_progress.setVisibility(View.GONE);
    this.getActivity().setProgressBarIndeterminateVisibility(false);
  }

  @Override public void showRetry() {
//    this.rl_retry.setVisibility(View.VISIBLE);
  }

  @Override public void hideRetry() {
//    this.rl_retry.setVisibility(View.GONE);
  }

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

  @Override public Context context() {
    return this.getActivity().getApplicationContext();
  }

  private void setupRecyclerView() {
    this.rv_list.setLayoutManager(new ListLayoutManager(context()));
    this.rv_list.setAdapter(listAdapter);
  }
}
