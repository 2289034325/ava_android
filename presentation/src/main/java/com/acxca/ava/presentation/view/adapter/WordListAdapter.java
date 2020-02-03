/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.model.UserModel;
import com.acxca.domain.Speech;
import com.acxca.domain.Word;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link UserModel}.
 */
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ListViewHolder> {
  private List<Word> items;
  private final LayoutInflater layoutInflater;

  @Inject
  WordListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.items = new ArrayList();
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.row_word_list, parent, false);
    return new ListViewHolder(view);
  }

  @Override public void onBindViewHolder(ListViewHolder holder, final int position) {
    final Word item = this.items.get(position);

    holder.tv_spell.setText(item.getSpell());
    holder.tv_pronounce.setText(String.format("/%s/",item.getPronounce()));
    holder.tv_meaning.setText(item.getMeaning());
    holder.tv_phase.setText(String.valueOf(item.getLearn_phase()));
    holder.tv_times.setText(String.valueOf(item.getAnswer_times()));
    float wrongPercentage = item.getAnswer_times()==0?0:item.getWrong_times()/item.getAnswer_times();
    String wp = String.valueOf(Math.round(wrongPercentage * 100.0) / 100.0);
    holder.tv_percentage.setText(String.format("s%%",wp));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setItems(List<Word> wordList) {
    this.items = wordList;
    this.notifyDataSetChanged();
  }

  public void appendItems(List<Word> wordList) {
    this.items.addAll(wordList);
    this.notifyDataSetChanged();
  }

  static class ListViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tv_spell) TextView tv_spell;
    @Bind(R.id.tv_pronounce) TextView tv_pronounce;
    @Bind(R.id.tv_meaning) TextView tv_meaning;
    @Bind(R.id.tv_phase) TextView tv_phase;
    @Bind(R.id.tv_times) TextView tv_times;
    @Bind(R.id.tv_percentage) TextView tv_percentage;

    ListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
