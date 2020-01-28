package com.acxca.ava.presentation.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.domain.Word;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TranslateDialogFragment extends BaseDialogFragment implements View.OnClickListener{
    private Dialog mDialog;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private String caller;

    private TextView tv_spell;
    private TextView tv_pronounce;


    private TextView mTvCancel;
    private TextView mTvOK;

    private Word word;

    public void setWord(Word w){
        this.word = w;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, android.support.v7.appcompat.R.style.Theme_AppCompat_Dialog);
            mDialog.setContentView(R.layout.dialog_word_meaning);

            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            mTvCancel = mDialog.findViewById(R.id.tv_cancel);
            mTvOK = mDialog.findViewById(R.id.tv_ok);

            mTvCancel.setOnClickListener(this);
            mTvOK.setOnClickListener(this);

            tv_spell = mDialog.findViewById(R.id.tv_spell);
            tv_spell.setText(this.word.getSpell());
            tv_pronounce = mDialog.findViewById(R.id.tv_pronounce);
            tv_pronounce.setText("["+this.word.getPronounce()+"]");

            initWindow();
        }
        return mDialog;
    }

    public void init(Context context, FragmentManager fragmentManager, String caller) {
        mContext = context;
        mFragmentManager = fragmentManager;
        caller = caller;
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
