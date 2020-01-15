package com.fernandocejas.android10.sample.presentation.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.fernandocejas.android10.sample.presentation.R;

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
