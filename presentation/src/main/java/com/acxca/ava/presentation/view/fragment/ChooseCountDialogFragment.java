package com.acxca.ava.presentation.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.consts.Lang;
import com.acxca.ava.presentation.presenter.WordStatListPresenter;
import com.acxca.ava.presentation.view.activity.MainActivity;
import com.acxca.ava.presentation.view.activity.WordViewActivity;
import com.acxca.domain.Explain;
import com.acxca.domain.Sentence;
import com.acxca.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class ChooseCountDialogFragment extends BaseDialogFragment implements View.OnClickListener{
    private Dialog mDialog;
    private Context mContext;
    private FragmentManager mFragmentManager;

    private TextView mTvCancel;
    private TextView mTvOK;
    private NumberPicker mNumberPicker;

    private int lang;
    private String mode;
    private WordStatListPresenter wordStatListPresenter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, android.support.v7.appcompat.R.style.Theme_AppCompat_Dialog);
            mDialog.setContentView(R.layout.dialog_choose_count);

            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            mTvCancel = mDialog.findViewById(R.id.tv_cancel);
            mTvOK = mDialog.findViewById(R.id.tv_ok);
            mNumberPicker = mDialog.findViewById(R.id.np_count);
            mNumberPicker.setMinValue(5);
            mNumberPicker.setMaxValue(50);

//            String[] minuteValues = new String[10];
//
//            for (int i = 1; i < minuteValues.length+1; i++) {
//                String number = Integer.toString(i*5);
//                minuteValues[i-1] = number;
//            }
//            mNumberPicker.setDisplayedValues(minuteValues);
            mNumberPicker.setValue(5);

            mTvCancel.setOnClickListener(this);
            mTvOK.setOnClickListener(this);
            initWindow();
        }
        return mDialog;
    }

    public void init(Context context, FragmentManager fragmentManager, String mode, int lang, WordStatListPresenter wordStatListPresenter) {
        mContext = context;
        mFragmentManager = fragmentManager;
        this.lang = lang;
        this.mode = mode;
        this.wordStatListPresenter = wordStatListPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                doOK();
                dismiss();
                break;
        }
    }

    private void doOK() {
        if("learn".equals(mode)){
            int count = mNumberPicker.getValue();
            wordStatListPresenter.loadNewWords(Lang.fromId(lang),count);
        }
        else{

        }
    }

    private void initWindow() {
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }


    public void show() {
        show(mFragmentManager, this.getClass().getSimpleName());
    }
}
