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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.presenter.WordStatListPresenter;
import com.acxca.ava.presentation.view.WordStatListView;
import com.acxca.ava.presentation.view.adapter.UserWordStatListAdapter;
import com.acxca.ava.presentation.view.adapter.UsersLayoutManager;
import com.acxca.domain.UserWordStat;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows a list of Users.
 */
public class WordStatListFragment extends BaseFragment implements WordStatListView {

  /**
   * Interface for listening user list events.
   */
  public interface WordStatListListener {
    void onWordStatItemClicked();
  }

  @Inject
  WordStatListPresenter wordStatListPresenter;

  @Inject
  UserWordStatListAdapter listAdapter;

  @Bind(R.id.rv_word_stat) RecyclerView rv_word_stat;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.rl_retry) RelativeLayout rl_retry;
  @Bind(R.id.bt_retry) Button bt_retry;

  private WordStatListListener wordStatListListener;

  public WordStatListFragment() {
    setRetainInstance(true);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof WordStatListListener) {
      this.wordStatListListener = (WordStatListListener) activity;
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(DictionaryComponent.class).inject(this);

    TextView title = (TextView) getActivity().findViewById(R.id.tv_title);
    title.setText("词汇");
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_word_stat_list, container, false);
    ButterKnife.bind(this, fragmentView);
    setupRecyclerView();
    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.wordStatListPresenter.setView(this);
    if (savedInstanceState == null) {
      this.wordStatListPresenter.loadUserWordStatList();
    }
  }

  @Override public void onResume() {
    super.onResume();
    this.wordStatListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.wordStatListPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    rv_word_stat.setAdapter(null);
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.wordStatListPresenter.destroy();
  }

  @Override public void onDetach() {
    super.onDetach();
    this.wordStatListListener = null;
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
    this.rl_retry.setVisibility(View.VISIBLE);
  }

  @Override public void hideRetry() {
    this.rl_retry.setVisibility(View.GONE);
  }

  @Override public void renderWordStatList(List<UserWordStat> userWordStatList) {
    if (userWordStatList != null) {
      this.listAdapter.setItems(userWordStatList);
    }
  }

  @Override public void showMenu() {
    if (this.wordStatListListener != null) {
      this.wordStatListListener.onWordStatItemClicked();
    }
  }

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

  @Override public Context context() {
    return this.getActivity().getApplicationContext();
  }

  private void setupRecyclerView() {
    this.listAdapter.setOnItemClickListener(onItemClickListener);
    this.rv_word_stat.setLayoutManager(new UsersLayoutManager(context()));
    this.rv_word_stat.setAdapter(listAdapter);
  }

  @OnClick(R.id.bt_retry) void onButtonRetryClick() {
    this.wordStatListPresenter.loadUserWordStatList();
  }

  private UserWordStatListAdapter.OnItemClickListener onItemClickListener =
      new UserWordStatListAdapter.OnItemClickListener() {
        @Override public void onItemClicked(UserWordStat userWordStat) {
          if (WordStatListFragment.this.wordStatListPresenter != null && userWordStat != null) {
            WordStatListFragment.this.wordStatListPresenter.onItemClicked();
          }
        }
      };
}
