/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.fragment;

import android.app.Activity;
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
import com.acxca.ava.presentation.di.components.DaggerReadingComponent;
import com.acxca.ava.presentation.di.components.DaggerSpeechComponent;
import com.acxca.ava.presentation.di.components.ReadingComponent;
import com.acxca.ava.presentation.di.components.SpeechComponent;
import com.acxca.ava.presentation.di.modules.ActivityModule;
import com.acxca.ava.presentation.presenter.BookMarkListPresenter;
import com.acxca.ava.presentation.presenter.SpeechkListPresenter;
import com.acxca.ava.presentation.view.BookMarkListView;
import com.acxca.ava.presentation.view.SpeechkListView;
import com.acxca.ava.presentation.view.adapter.BookMarkListAdapter;
import com.acxca.ava.presentation.view.adapter.ListLayoutManager;
import com.acxca.ava.presentation.view.adapter.SpeechListAdapter;
import com.acxca.domain.BookMark;
import com.acxca.domain.Speech;
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
public class SpeechListFragment extends BaseFragment implements SpeechkListView,HasComponent<SpeechComponent> {


  @Override
  public SpeechComponent getComponent() {
    return null;
  }

  @Override
  public void openSpeech(Speech speech) {

  }

  /**
   * Interface for listening user list events.
   */
  public interface ListListener {
    void onItemClicked(Speech speech);
  }

  @Inject
  SpeechkListPresenter speechkListPresenter;

  @Inject
  SpeechListAdapter listAdapter;

  private SpeechComponent speechComponent;

  @Bind(R.id.rv_list) RecyclerView rv_list;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.sl_wrapper) RefreshLayout sl_wrapper;

  private ListListener listListener;

  public SpeechListFragment() {
    setRetainInstance(true);
  }

  @Override
  public void renderSpeechList(List<Speech> speechList) {
    if (speechList != null) {
      sl_wrapper.finishRefresh(1);//传入false表示刷新失败
      this.listAdapter.setItems(speechList);
    }
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof ListListener) {
      this.listListener = (ListListener) activity;
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ApplicationComponent appc = ((AndroidApplication) getActivity().getApplication()).getApplicationComponent();
    this.speechComponent = DaggerSpeechComponent.builder()
            .applicationComponent(appc)
            .activityModule(new ActivityModule(getActivity()))
            .build();

    this.speechComponent.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_item_list_with_refresh, container, false);
    ButterKnife.bind(this, fragmentView);
    setupRecyclerView();

    sl_wrapper.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(RefreshLayout refreshlayout) {
        speechkListPresenter.loadSpeechList();
      }
    });
    sl_wrapper.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override
      public void onLoadMore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
      }
    });


    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.speechkListPresenter.setView(this);
    if (savedInstanceState == null) {
      this.speechkListPresenter.loadSpeechList();
    }
  }

  @Override public void onResume() {
    super.onResume();
    this.speechkListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.speechkListPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    rv_list.setAdapter(null);
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.speechkListPresenter.destroy();
  }

  @Override public void onDetach() {
    super.onDetach();
    this.listListener = null;
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
    this.listAdapter.setOnItemClickListener(onItemClickListener);
    this.rv_list.setLayoutManager(new ListLayoutManager(context()));
    this.rv_list.setAdapter(listAdapter);
  }

  private SpeechListAdapter.OnItemClickListener onItemClickListener =
      new SpeechListAdapter.OnItemClickListener() {
        @Override public void onItemClicked(Speech speech) {
          if (SpeechListFragment.this.speechkListPresenter != null && speech != null) {
            SpeechListFragment.this.speechkListPresenter.onItemClicked(speech);
            SpeechListFragment.this.speechkListPresenter.onItemClicked(speech);
          }
        }
      };
}
