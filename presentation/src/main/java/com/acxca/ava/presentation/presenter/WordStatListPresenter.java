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
import com.acxca.ava.presentation.mapper.UserModelDataMapper;
import com.acxca.ava.presentation.model.UserModel;
import com.acxca.ava.presentation.view.WordStatListView;
import com.acxca.domain.User;
import com.acxca.domain.UserWordStat;
import com.acxca.domain.exception.DefaultErrorBundle;
import com.acxca.domain.exception.ErrorBundle;
import com.acxca.domain.interactor.DefaultObserver;
import com.acxca.domain.interactor.GetUserWordStatList;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class WordStatListPresenter implements Presenter {

  private WordStatListView viewListView;

  private final GetUserWordStatList getUserWordStatListUseCase;

  @Inject
  public WordStatListPresenter(GetUserWordStatList getUserWordStatListUseCase) {
    this.getUserWordStatListUseCase = getUserWordStatListUseCase;
  }

  public void setView(@NonNull WordStatListView view) {
    this.viewListView = view;
  }

  @Override public void resume() {}

  @Override public void pause() {}

  @Override public void destroy() {
    this.getUserWordStatListUseCase.dispose();
    this.viewListView = null;
  }

  /**
   * Loads all users.
   */
  public void loadUserWordStatList() {
    this.hideViewRetry();
    this.showViewLoading();
    this.getUserWordStatListUseCase.execute(new UserWordStatListObserver(), null);
  }

  public void onItemClicked() {
    this.viewListView.showMenu();
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

  private void showUsersCollectionInView(List<UserWordStat> userWordStatList) {
    this.viewListView.renderWordStatList(userWordStatList);
  }

  private final class UserWordStatListObserver extends DefaultObserver<List<UserWordStat>> {

    @Override public void onComplete() {
      WordStatListPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      WordStatListPresenter.this.hideViewLoading();
      WordStatListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      WordStatListPresenter.this.showViewRetry();
    }

    @Override public void onNext(List<UserWordStat> userWordStatList) {
      WordStatListPresenter.this.showUsersCollectionInView(userWordStatList);
    }
  }
}
