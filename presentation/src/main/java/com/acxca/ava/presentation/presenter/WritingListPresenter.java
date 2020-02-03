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
import com.acxca.ava.presentation.view.WritingListView;
import com.acxca.domain.WritingArticle;
import com.acxca.domain.exception.DefaultErrorBundle;
import com.acxca.domain.exception.ErrorBundle;
import com.acxca.domain.interactor.DefaultObserver;
import com.acxca.domain.interactor.GetWritingArticleList;

import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class WritingListPresenter implements Presenter {

  private WritingListView viewListView;

  private final GetWritingArticleList getWritingArticleListUseCase;

  @Inject
  public WritingListPresenter(GetWritingArticleList getWritingArticleListUseCase) {
    this.getWritingArticleListUseCase = getWritingArticleListUseCase;
  }

  public void setView(@NonNull WritingListView view) {
    this.viewListView = view;
  }

  @Override public void resume() {}

  @Override public void pause() {}

  @Override public void destroy() {
    this.getWritingArticleListUseCase.dispose();
    this.viewListView = null;
  }

  /**
   * Loads all users.
   */
  public void loadArticleList() {
    this.hideViewRetry();
    this.showViewLoading();
    this.getWritingArticleListUseCase.execute(new ArticleListObserver(), null);
  }

  public void onItemClicked(WritingArticle article) {
    this.viewListView.openArticle(article);
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

  private final class ArticleListObserver extends DefaultObserver<List<WritingArticle>> {

    @Override public void onComplete() {
      WritingListPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      WritingListPresenter.this.hideViewLoading();
      WritingListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      WritingListPresenter.this.showViewRetry();
    }

    @Override public void onNext(List<WritingArticle> articleList) {
      WritingListPresenter.this.viewListView.renderArticleList(articleList);
    }
  }
}
