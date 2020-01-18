/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.components.UserComponent;
import com.acxca.ava.presentation.presenter.LoginPresenter;
import com.acxca.ava.presentation.view.LoginView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows a list of Users.
 */
public class LoginFragment extends BaseFragment implements LoginView {


  @Inject LoginPresenter loginPresenter;

  @Bind(R.id.img_kapatcha) ImageView img_kapatcha;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.rl_retry) RelativeLayout rl_retry;
  @Bind(R.id.bt_retry) Button bt_retry;

  public LoginFragment() {
    setRetainInstance(true);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(UserComponent.class).inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
    ButterKnife.bind(this, fragmentView);
    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.loginPresenter.setView(this);
    if (savedInstanceState == null) {
      this.loadKaptcha();
    }
  }

  @Override public void onResume() {
    super.onResume();
    this.loginPresenter.resume();
  }

  @Override public void onPause() {
    super.onPause();
    this.loginPresenter.pause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    this.loginPresenter.destroy();
  }

  @Override public void onDetach() {
    super.onDetach();
    this.loginPresenter = null;
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

  @Override public void showError(String message) {
    this.showToastMessage(message);
  }

  @Override public Context context() {
    return this.getActivity().getApplicationContext();
  }

  /**
   * Loads all users.
   */
  private void loadKaptcha() {
    this.loginPresenter.initialize();
  }

  @OnClick(R.id.bt_retry) void onButtonRetryClick() {
    LoginFragment.this.loadKaptcha();
  }

  @OnClick(R.id.img_kapatcha) void onKaptchaClick() {
    LoginFragment.this.loadKaptcha();
  }

  @OnClick(R.id.btn_login) void onLoginClick() {

  }

  @Override
  public void renderKaptcha(String imgString) {
    byte[] decodedString = Base64.decode(imgString, Base64.DEFAULT);
    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    img_kapatcha.setImageBitmap(decodedByte);
  }
}
