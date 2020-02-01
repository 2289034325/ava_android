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

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.ApplicationComponent;
import com.acxca.ava.presentation.di.components.DaggerReadingComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.di.components.ReadingComponent;
import com.acxca.ava.presentation.di.modules.ActivityModule;
import com.acxca.ava.presentation.presenter.BookMarkListPresenter;
import com.acxca.ava.presentation.presenter.WordStatListPresenter;
import com.acxca.ava.presentation.view.BookMarkListView;
import com.acxca.ava.presentation.view.WordStatListView;
import com.acxca.ava.presentation.view.adapter.BookMarkListAdapter;
import com.acxca.ava.presentation.view.adapter.ListLayoutManager;
import com.acxca.ava.presentation.view.adapter.UserWordStatListAdapter;
import com.acxca.ava.presentation.view.adapter.UsersLayoutManager;
import com.acxca.domain.BookMark;
import com.acxca.domain.UserWordStat;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows a list of Users.
 */
public class BookMarkListFragment extends BaseFragment implements BookMarkListView,HasComponent<ReadingComponent> {


  @Override
  public ReadingComponent getComponent() {
    return null;
  }

  @Override
  public void openBookMark(BookMark bookMark) {

  }

  /**
   * Interface for listening user list events.
   */
  public interface BookMarkListListener {
    void onBookMarkItemClicked(BookMark bookMark);
  }

  @Inject
  BookMarkListPresenter bookMarkListPresenter;

  @Inject
  BookMarkListAdapter listAdapter;

  private ReadingComponent readingComponent;

  @Bind(R.id.rv_bookmark) RecyclerView rv_bookmark;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.refreshLayout) RefreshLayout refreshLayout;

//  @Bind(R.id.rl_retry) RelativeLayout rl_retry;
//  @Bind(R.id.bt_retry) Button bt_retry;

  private BookMarkListListener bookMarkListListener;

  public BookMarkListFragment() {
    setRetainInstance(true);
  }

  @Override
  public void renderBookMarkList(List<BookMark> bookMarkList) {
    if (bookMarkList != null) {
      refreshLayout.finishRefresh(1);//传入false表示刷新失败
      this.listAdapter.setItems(bookMarkList);
    }
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    if (activity instanceof BookMarkListListener) {
      this.bookMarkListListener = (BookMarkListListener) activity;
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ApplicationComponent appc = ((AndroidApplication) getActivity().getApplication()).getApplicationComponent();
    this.readingComponent = DaggerReadingComponent.builder()
            .applicationComponent(appc)
            .activityModule(new ActivityModule(getActivity()))
            .build();

    this.readingComponent.inject(this);

//    this.getComponent(ReadingComponent.class).inject(this);

  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
    ButterKnife.bind(this, fragmentView);
    setupRecyclerView();

    refreshLayout.setOnRefreshListener(new OnRefreshListener() {
      @Override
      public void onRefresh(RefreshLayout refreshlayout) {
        bookMarkListPresenter.loadBookMarkList();
      }
    });
    refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
      @Override
      public void onLoadMore(RefreshLayout refreshlayout) {
        refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
      }
    });


    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.bookMarkListPresenter.setView(this);
    if (savedInstanceState == null) {
      this.bookMarkListPresenter.loadBookMarkList();
    }
  }

  @Override public void onResume() {
    super.onResume();
    this.bookMarkListPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.bookMarkListPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    rv_bookmark.setAdapter(null);
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.bookMarkListPresenter.destroy();
  }

  @Override public void onDetach() {
    super.onDetach();
    this.bookMarkListListener = null;
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
    this.rv_bookmark.setLayoutManager(new ListLayoutManager(context()));
    this.rv_bookmark.setAdapter(listAdapter);
  }

  private BookMarkListAdapter.OnItemClickListener onItemClickListener =
      new BookMarkListAdapter.OnItemClickListener() {
        @Override public void onItemClicked(BookMark bookMark) {
          if (BookMarkListFragment.this.bookMarkListPresenter != null && bookMark != null) {
            BookMarkListFragment.this.bookMarkListListener.onBookMarkItemClicked(bookMark);
            BookMarkListFragment.this.bookMarkListPresenter.onItemClicked(bookMark);
          }
        }
      };
}
