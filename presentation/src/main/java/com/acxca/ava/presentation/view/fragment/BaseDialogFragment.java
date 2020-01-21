package com.acxca.ava.presentation.view.fragment;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by lion on 17-11-27.
 */

public class BaseDialogFragment extends DialogFragment {
    private static final String TAG = "====_BaseDialogFragment";


    @Override
    public void show(FragmentManager manager, String tag) {
        if (isAdded()) {
            return;
        }
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) {
            //try commit  after an activity's state is saved
            Log.w(TAG, "try to show a DialogFragment allowing StateLoss");
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }
}
