package com.acxca.ava.presentation.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    checkLogin();
  }

  private void checkLogin() {
    SharedPreferences sp = getSharedPreferences("user", 0);
    String token = sp.getString("token", null);

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
