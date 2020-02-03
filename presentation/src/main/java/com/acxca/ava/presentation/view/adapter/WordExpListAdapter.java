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
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.model.UserModel;
import com.acxca.domain.Explain;
import com.acxca.domain.Sentence;
import com.acxca.domain.Word;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link UserModel}.
 */
public class WordExpListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private List<Object> items;
  private final LayoutInflater layoutInflater;


  public WordExpListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    items = new ArrayList<>();
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if(viewType == 0){
      final View view = this.layoutInflater.inflate(R.layout.row_wordview_explain, parent, false);
      return new ExplainViewHolder(view);
    }
    else{
      final View view = this.layoutInflater.inflate(R.layout.row_wordview_sentence, parent, false);
      return new SentenceViewHolder(view);
    }

  }

  @Override public int getItemViewType(int position) {

    Object obj = items.get(position);
    if(obj instanceof String){
      return 0;
    }
    else{
      return 1;
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    final Object item = this.items.get(position);

    if(holder instanceof ExplainViewHolder){
      String s = (String) item;
      ExplainViewHolder eh = (ExplainViewHolder) holder;
      eh.tv_meaning.setText(s);
    }
    else{
      Sentence s = (Sentence) item;
      SentenceViewHolder sh = (SentenceViewHolder) holder;
      sh.tv_sentence.setText(s.getSentence());
      sh.tv_translation.setText(s.getTranslation());
    }
  }

  public void setItem(Word word) {
    for(Explain exp : word.getExplains()){
      items.add(exp.getExplain());
      for(Sentence st : exp.getSentences()){
        items.add(st);
      }
    }
    this.notifyDataSetChanged();
  }


  @Override public long getItemId(int position) {
    return position;
  }

  static class ExplainViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tv_meaning) TextView tv_meaning;

    ExplainViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  static class SentenceViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.tv_sentence) TextView tv_sentence;
    @Bind(R.id.tv_translation) TextView tv_translation;

    SentenceViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
