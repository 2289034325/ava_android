/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view;

import com.acxca.ava.presentation.model.UserModel;

import java.util.Collection;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of {@link UserModel}.
 */
public interface LoginView extends LoadDataView {
  /**
   * 将字符串转换成图片，显示在imageView中
   * @param imgString
   */
  void renderKaptcha(String imgString);

  void onLoginSuccess(String token);
}
