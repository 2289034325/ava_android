package com.acxca.ava.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.consts.Consts;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.di.HasComponent;
import com.acxca.ava.presentation.di.components.DaggerDictionaryComponent;
import com.acxca.ava.presentation.di.components.DictionaryComponent;
import com.acxca.ava.presentation.view.fragment.WordStatListFragment;

/**
 * Main application screen. This is the app entry point.
 */
public class MainActivity extends BaseActivity implements HasComponent<DictionaryComponent>,
        WordStatListFragment.WordStatListListener{

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, MainActivity.class);
        return callingIntent;
    }

    private DictionaryComponent dictionaryComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dictionaryComponent = DaggerDictionaryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();

        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences sp = getSharedPreferences(Consts.SP_NAME, 0);
        String token = sp.getString(Consts.SP_KEY_TOKEN, null);

        if (token == null) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);

            finish();
        } else {
            AndroidApplication app = (AndroidApplication)this.getApplicationContext();
            app.sharedBag.put(Consts.SB_KEY_TOKEN,token);

            setContentView(R.layout.activity_main);
            findViewById(R.id.iv_back).setVisibility(View.GONE);

            addFragment(R.id.container, new WordStatListFragment());
        }
    }

    @Override
    public DictionaryComponent getComponent() {
        return dictionaryComponent;
    }

    @Override
    public void onWordStatItemClicked() {
        showToastMessage("lang clicked");
    }
}
