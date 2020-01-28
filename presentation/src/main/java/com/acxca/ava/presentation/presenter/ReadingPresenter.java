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
import com.acxca.ava.presentation.view.ReadingView;
import com.acxca.domain.Word;
import com.acxca.domain.exception.DefaultErrorBundle;
import com.acxca.domain.exception.ErrorBundle;
import com.acxca.domain.interactor.DefaultObserver;
import com.acxca.domain.interactor.SearchWord;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class ReadingPresenter implements Presenter {

  private ReadingView readingView;
  private final SearchWord searchWordUseCase;

  @Inject
  public ReadingPresenter(SearchWord searchWordUseCase) {
    this.searchWordUseCase = searchWordUseCase;
  }

  @Override public void resume() {}

  @Override public void pause() {}

  @Override public void destroy() {
    this.searchWordUseCase.dispose();
    this.readingView = null;
  }

  public void setView(@NonNull ReadingView view) {
    this.readingView = view;
  }

  private void showViewLoading() {
    this.readingView.showLoading();
  }

  public void searchWord(int lang,String form) {
    this.showViewLoading();
    this.searchWordUseCase.execute(new SearchWordObserver(), SearchWord.Params.getParmas(lang,form));
  }

  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.readingView.context(),
            errorBundle.getException());
    this.readingView.showError(errorMessage);
  }

  private void hideViewLoading() {
    this.readingView.hideLoading();
  }

  private final class SearchWordObserver extends DefaultObserver<Word> {

    @Override public void onComplete() {
      ReadingPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      ReadingPresenter.this.hideViewLoading();
      ReadingPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
    }

    @Override public void onNext(Word word) {
      ReadingPresenter.this.readingView.showTranslateWord(word);
    }
  }
}
