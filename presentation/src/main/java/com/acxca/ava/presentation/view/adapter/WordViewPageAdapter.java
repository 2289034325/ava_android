package com.acxca.ava.presentation.view.adapter;


import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.domain.Word;

import java.util.List;

public class WordViewPageAdapter extends PagerAdapter {
    List<Word> wordList;
    public WordViewPageAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.view_word_info, null);

        Word word = wordList.get(position);
        TextView tv_spell = v.findViewById(R.id.tv_spell);
        tv_spell.setText(word.getSpell());
        TextView tv_pronounce = v.findViewById(R.id.tv_pronounce);
        tv_pronounce.setText(word.getPronounce());
        TextView tv_meaning = v.findViewById(R.id.tv_meaning);
        tv_meaning.setText(word.getMeaning());

        RecyclerView rv = v.findViewById(R.id.rv_exps);
        rv.setLayoutManager(new ListLayoutManager(container.getContext()));
        WordExpListAdapter wid = new WordExpListAdapter(container.getContext());
        wid.setItem(wordList.get(position));
        rv.setAdapter(wid);

        container.addView(v);
        return v;
    }
}
