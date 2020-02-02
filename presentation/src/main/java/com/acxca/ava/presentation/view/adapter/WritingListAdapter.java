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
import com.acxca.domain.WritingArticle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link UserModel}.
 */
public class WritingListAdapter extends RecyclerView.Adapter<WritingListAdapter.ListViewHolder> {

  public interface OnItemClickListener {
    void onItemClicked(WritingArticle article);
  }

  private List<WritingArticle> items;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  @Inject
  WritingListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.items = new ArrayList();
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.row_writing, parent, false);
    return new ListViewHolder(view);
  }

  @Override public void onBindViewHolder(ListViewHolder holder, final int position) {
    final WritingArticle item = this.items.get(position);

    holder.tv_title.setText(item.getTitle());
    holder.tv_description.setText(item.getDescription());
    holder.tv_source.setText(item.getSource());

    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (WritingListAdapter.this.onItemClickListener != null) {
          WritingListAdapter.this.onItemClickListener.onItemClicked(item);
        }
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setItems(List<WritingArticle> articleList) {
    this.items = articleList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  static class ListViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.iv_flg) ImageView iv_flg;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_source) TextView tv_source;
    @Bind(R.id.tv_description) TextView tv_description;

    ListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
