/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.DaggerUserComponent;
import com.acxca.ava.presentation.di.components.UserComponent;
import com.acxca.ava.presentation.model.UserModel;
import com.acxca.ava.presentation.view.fragment.LoginFragment;
import com.acxca.ava.presentation.view.fragment.UserListFragment;
import com.fernandocejas.android10.sample.presentation.R;

/**
 * Activity that shows a list of Users.
 */
public class LoginActivity extends BaseActivity implements HasComponent<UserComponent>{

  public static Intent getCallingIntent(Context context) {
    return new Intent(context, LoginActivity.class);
  }

  private UserComponent userComponent;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(R.layout.activity_layout);

    this.initializeInjector();
    if (savedInstanceState == null) {
      addFragment(R.id.fragmentContainer, new LoginFragment());
    }
  }

  private void initializeInjector() {
    this.userComponent = DaggerUserComponent.builder()
        .applicationComponent(getApplicationComponent())
        .activityModule(getActivityModule())
        .build();
  }

  @Override public UserComponent getComponent() {
    return userComponent;
  }
}
