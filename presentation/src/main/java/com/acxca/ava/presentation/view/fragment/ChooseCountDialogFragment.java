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

    private String mode;

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

    public void init(Context context, FragmentManager fragmentManager, String mode) {
        mContext = context;
        mFragmentManager = fragmentManager;
        this.mode = mode;
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

            List<Word> wordList = createWords();

            Intent intentToLaunch = WordViewActivity.getCallingIntent(getContext(), wordList);
            getActivity().startActivity(intentToLaunch);
        }
        else{

        }
    }

    private List<Word> createWords(){
        Sentence s = new Sentence();
        s.setSentence("Hello android!1");
        s.setTranslation("你好 天才1！");
        List<Sentence> ss = new ArrayList<>();
        ss.add(s);
        Explain exp = new Explain();
        exp.setExplain("问候1");
        exp.setSentences(ss);

        Sentence s2 = new Sentence();
        s2.setSentence("Hello android!2");
        s2.setTranslation("你好 天才！2");
        List<Sentence> ss2 = new ArrayList<>();
        ss2.add(s2);
        Explain exp2 = new Explain();
        exp2.setExplain("问候2");
        exp2.setSentences(ss2);

        Word w = new Word();
        w.setSpell("Talented");
        w.setPronounce("hello");
        w.setMeaning("你好");
        List<Explain> exps = new ArrayList<>();
        exps.add(exp);
        exps.add(exp2);
        w.setExplains(exps);

        Sentence s3 = new Sentence();
        s3.setSentence("Hello android!");
        s3.setTranslation("你好 天才！");
        List<Sentence> ss3 = new ArrayList<>();
        ss3.add(s3);
        Explain exp3 = new Explain();
        exp3.setExplain("问候");
        exp3.setSentences(ss3);

        Sentence s4 = new Sentence();
        s4.setSentence("Hello android!");
        s4.setTranslation("你好 天才！");
        List<Sentence> ss4 = new ArrayList<>();
        ss4.add(s4);
        Explain exp4 = new Explain();
        exp4.setExplain("问候");
        exp4.setSentences(ss4);

        Word w2 = new Word();
        w2.setSpell("Hello");
        w2.setPronounce("hello");
        w2.setMeaning("你好");
        List<Explain> exps2 = new ArrayList<>();
        exps2.add(exp3);
        exps2.add(exp4);
        w2.setExplains(exps2);

        List<Word> wordList = new ArrayList<>();
        wordList.add(w);
        wordList.add(w2);

        return wordList;
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
