/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acxca.ava.presentation.presenter;

import android.support.annotation.NonNull;

import com.acxca.ava.presentation.di.PerActivity;
import com.acxca.ava.presentation.exception.ErrorMessageFactory;
import com.acxca.ava.presentation.view.WordListView;
import com.acxca.ava.presentation.view.WordStatListView;
import com.acxca.domain.UserWordStat;
import com.acxca.domain.Word;
import com.acxca.domain.exception.DefaultErrorBundle;
import com.acxca.domain.exception.ErrorBundle;
import com.acxca.domain.interactor.DefaultObserver;
import com.acxca.domain.interactor.GetUserWordList;
import com.acxca.domain.interactor.GetUserWordStatList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class WordListPresenter implements Presenter {

  private WordListView viewListView;

  private final GetUserWordList getUserWordListUseCase;

  @Inject
  public WordListPresenter(GetUserWordList getUserWordListUseCase) {
    this.getUserWordListUseCase = getUserWordListUseCase;
  }

  public void setView(@NonNull WordListView view) {
    this.viewListView = view;
  }

  @Override public void resume() {}

  @Override public void pause() {}

  @Override public void destroy() {
    this.getUserWordListUseCase.dispose();
    this.viewListView = null;
  }

  /**
   * Loads all users.
   */
  public void loadUserWordList(int lang,int pageIndex,int pageSize) {
    this.hideViewRetry();
    this.showViewLoading();
    Map params = new HashMap();
    params.put("lang",lang);
    params.put("pageIndex",pageIndex);
    params.put("pageSize",pageSize);
    this.getUserWordListUseCase.execute(new UserWordListObserver(pageIndex==0), params);
  }

  private void showViewLoading() {
    this.viewListView.showLoading();
  }

  private void hideViewLoading() {
    this.viewListView.hideLoading();
  }

  private void showViewRetry() {
    this.viewListView.showRetry();
  }

  private void hideViewRetry() {
    this.viewListView.hideRetry();
  }

  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.viewListView.context(),
        errorBundle.getException());
    this.viewListView.showError(errorMessage);
  }

  private final class UserWordListObserver extends DefaultObserver<List<Word>> {
    private  boolean isFirstPage;
    public UserWordListObserver(boolean isFirstPage){
      this.isFirstPage = isFirstPage;
    }

    @Override public void onComplete() {
      WordListPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      WordListPresenter.this.hideViewLoading();
      WordListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      WordListPresenter.this.showViewRetry();
    }

    @Override public void onNext(List<Word> wordList) {
      if(isFirstPage){
        WordListPresenter.this.viewListView.renderWordList(wordList);
      }
      else{
        WordListPresenter.this.viewListView.appendWordList(wordList);
      }
    }
  }
}
