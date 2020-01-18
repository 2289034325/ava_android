package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.acxca.ava.presentation.Consts;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity {

  public static Intent getCallingIntent(Context context) {
    Intent callingIntent = new Intent(context, MainActivity.class);
    return callingIntent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    checkLogin();
  }

  private void checkLogin() {
    SharedPreferences sp = getSharedPreferences(Consts.SP_NAME, 0);
    String token = sp.getString(Consts.SP_KEY_TOKEN, null);

    if (token == null) {
      Intent i = new Intent(MainActivity.this, LoginActivity.class);
      startActivity(i);
    } else {
      Intent i = new Intent(MainActivity.this, UserListActivity.class);
      startActivity(i);
    }

    finish();
  }
}
