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
import android.widget.EditText;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.presenter.ReadingPresenter;
import com.acxca.domain.BookMark;

import javax.inject.Inject;

public class SaveBookmarkDialogFragment extends BaseDialogFragment implements View.OnClickListener{
    private Dialog mDialog;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private BookMark mbookMark;

    private TextView mTvCancel;
    private TextView mTvOK;
    private EditText txb_name;
    private EditText txb_title;

    public ReadingPresenter readingPresenter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, android.support.v7.appcompat.R.style.Theme_AppCompat_Dialog);
            mDialog.setContentView(R.layout.dialog_save_bookmark);

            mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            mTvCancel = mDialog.findViewById(R.id.tv_cancel);
            mTvOK = mDialog.findViewById(R.id.tv_ok);

            txb_name = mDialog.findViewById(R.id.txb_name);
            txb_title = mDialog.findViewById(R.id.txb_title);

            if(this.mbookMark != null){
                this.txb_name.setText(mbookMark.name);
                this.txb_title.setText(mbookMark.title);
            }

            mTvCancel.setOnClickListener(this);
            mTvOK.setOnClickListener(this);
            initWindow();
        }
        return mDialog;
    }

    public void init(Context context, FragmentManager fragmentManager, BookMark bookMark) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mbookMark = bookMark;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ok:
                doOK();
                break;
        }
    }

    private void doOK() {
        this.mbookMark.name = txb_name.getText().toString();
        this.mbookMark.title = txb_title.getText().toString();

        readingPresenter.saveBookMark(this.mbookMark);
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
