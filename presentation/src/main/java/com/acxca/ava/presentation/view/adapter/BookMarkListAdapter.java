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

import com.acxca.ava.presentation.PresentationUtil;
import com.acxca.ava.presentation.R;
import com.acxca.ava.presentation.consts.Lang;
import com.acxca.ava.presentation.model.UserModel;
import com.acxca.domain.BookMark;
import com.acxca.domain.UserWordStat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link UserModel}.
 */
public class BookMarkListAdapter extends RecyclerView.Adapter<BookMarkListAdapter.ListViewHolder> {

  public interface OnItemClickListener {
    void onItemClicked(BookMark bookMark);
  }

  private List<BookMark> items;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  @Inject
  BookMarkListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.items = new ArrayList();
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.row_bookmark, parent, false);
    return new ListViewHolder(view);
  }

  @Override public void onBindViewHolder(ListViewHolder holder, final int position) {
    final BookMark item = this.items.get(position);

    holder.tv_title.setText(String.valueOf(item.getTitle()));
    holder.tv_name.setText(String.valueOf(item.getName()));


    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (BookMarkListAdapter.this.onItemClickListener != null) {
          BookMarkListAdapter.this.onItemClickListener.onItemClicked(item);
        }
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setItems(List<BookMark> bookMarkList) {
    this.items = bookMarkList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  static class ListViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.iv_icon) ImageView iv_icon;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_name) TextView tv_name;

    ListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
