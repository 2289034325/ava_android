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

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.Consts;
import com.acxca.ava.presentation.di.PerActivity;
import com.acxca.ava.presentation.exception.ErrorMessageFactory;
import com.acxca.ava.presentation.mapper.UserModelDataMapper;
import com.acxca.ava.presentation.model.UserModel;
import com.acxca.ava.presentation.view.LoginView;
import com.acxca.ava.presentation.view.UserListView;
import com.acxca.ava.presentation.view.fragment.LoginFragment;
import com.acxca.domain.User;
import com.acxca.domain.exception.DefaultErrorBundle;
import com.acxca.domain.exception.ErrorBundle;
import com.acxca.domain.interactor.DefaultObserver;
import com.acxca.domain.interactor.GetKaptcha;
import com.acxca.domain.interactor.GetUserList;
import com.acxca.domain.interactor.Login;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * {@link Presenter} that controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class LoginPresenter implements Presenter {

  private LoginView viewLoginView;

  private final GetKaptcha getKaptchaUseCase;
  private final Login loginUseCase;


  @Inject
  public LoginPresenter(GetKaptcha getKaptchaUseCase,Login loginUseCase) {
    this.getKaptchaUseCase = getKaptchaUseCase;
    this.loginUseCase = loginUseCase;
  }

  public void setView(@NonNull LoginView view) {
    this.viewLoginView = view;
  }

  @Override public void resume() {}

  @Override public void pause() {}

  @Override public void destroy() {
    this.getKaptchaUseCase.dispose();
    this.viewLoginView = null;
  }

  /**
   * Loads all users.
   */
  public void loadKaptcha() {
    this.hideViewRetry();
    this.showViewLoading();
    this.getKaptcha();
  }

  public void login(String username,String password,String code,String ticket){
    Map map = new HashMap();
    map.put("username",username);
    map.put("password",password);
    map.put("code",code);
    map.put("ticket",ticket);

    this.loginUseCase.execute(new LoginObserver(), map);
  }

  private void showViewLoading() {
    this.viewLoginView.showLoading();
  }

  private void hideViewLoading() {
    this.viewLoginView.hideLoading();
  }

  private void showViewRetry() {
    this.viewLoginView.showRetry();
  }

  private void hideViewRetry() {
    this.viewLoginView.hideRetry();
  }

  private void showErrorMessage(ErrorBundle errorBundle) {
    String errorMessage = ErrorMessageFactory.create(this.viewLoginView.context(),
        errorBundle.getException());
    this.viewLoginView.showError(errorMessage);
  }

  private void renderImageInView(String imgString) {
    this.viewLoginView.renderKaptcha(imgString);
  }

  private void onLoginSuccess(String token){
    this.viewLoginView.onLoginSuccess(token);
  }

  private void getKaptcha() {
    this.getKaptchaUseCase.execute(new KaptchaObserver(), null);
  }

  private final class KaptchaObserver extends DefaultObserver<Map<String,String>> {

    @Override public void onComplete() {
      LoginPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      LoginPresenter.this.hideViewLoading();
      LoginPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      LoginPresenter.this.showViewRetry();
    }

    @Override public void onNext(Map<String,String> kaptcha) {
      String ticket = kaptcha.get("ticket");
      AndroidApplication app =  (AndroidApplication)LoginPresenter.this.viewLoginView.context();
      app.shareBag.put(Consts.SB_KEY_TICKET,ticket);

      String img = kaptcha.get("img");
      LoginPresenter.this.renderImageInView(img);
    }
  }

  private final class LoginObserver extends DefaultObserver<String> {

    @Override public void onComplete() {
      LoginPresenter.this.hideViewLoading();
    }

    @Override public void onError(Throwable e) {
      LoginPresenter.this.hideViewLoading();
      LoginPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
      LoginPresenter.this.showViewRetry();
    }

    @Override public void onNext(String token) {
      LoginPresenter.this.onLoginSuccess(token);
    }
  }
}
