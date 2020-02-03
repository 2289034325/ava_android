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
import com.acxca.ava.presentation.view.SpeechListView;
import com.acxca.domain.Speech;
import com.acxca.domain.exception.DefaultErrorBundle;
import com.acxca.domain.exception.ErrorBundle;
import com.acxca.domain.interactor.DefaultObserver;
import com.acxca.domain.interactor.GetSpeechkList;

import java.util.List;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class SpeechListPresenter implements Presenter {

  private SpeechListView viewListView;

  private final GetSpeechkList getSpeechListUseCase;

  @Inject
  public SpeechListPresenter(GetSpeechkList getSpeechListUseCase) {
    this.getSpeechListUseCase = getSpeechListUseCase;
  }

  public void setView(@NonNull SpeechListView view) {
    this.viewListView = view;
  }

  @Override public void resume() {}

  @Override public void pause() {}

  @Override public void destroy() {
    this.getSpeechListUseCase.dispose();
    this.viewListView = null;
  }

  /**
   * Loads all users.
   */
  public void loadSpeechList() {
    this.hideViewRetry();
    this.showViewLoading();
    this.getSpeechListUseCase.execute(new SpeechListObserver(), null);
  }

  public void onItemClicked(Speech speech) {
    this.viewListView.openSpeech(speech);
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

  private final class SpeechListObserver extends DefaultObserver<List<Speech>> {

    @Override public void onComplete() {
      SpeechListPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      SpeechListPresenter.this.hideViewLoading();
      SpeechListPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      SpeechListPresenter.this.showViewRetry();
    }

    @Override public void onNext(List<Speech> speechList) {
      SpeechListPresenter.this.viewListView.renderSpeechList(speechList);
    }
  }
}
