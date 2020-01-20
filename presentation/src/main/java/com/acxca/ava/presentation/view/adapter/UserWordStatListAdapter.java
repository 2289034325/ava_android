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
import com.acxca.domain.UserWordStat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Adaptar that manages a collection of {@link UserModel}.
 */
public class UserWordStatListAdapter extends RecyclerView.Adapter<UserWordStatListAdapter.ListViewHolder> {

  public interface OnItemClickListener {
    void onItemClicked(UserWordStat userWordStat);
  }

  private List<UserWordStat> items;
  private final LayoutInflater layoutInflater;

  private OnItemClickListener onItemClickListener;

  @Inject
  UserWordStatListAdapter(Context context) {
    this.layoutInflater =
        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.items = new ArrayList();
  }

  @Override public int getItemCount() {
    return (this.items != null) ? this.items.size() : 0;
  }

  @Override public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    final View view = this.layoutInflater.inflate(R.layout.row_word_stat, parent, false);
    return new ListViewHolder(view);
  }

  @Override public void onBindViewHolder(ListViewHolder holder, final int position) {
    final UserWordStat item = this.items.get(position);

    Lang lang = Lang.fromId(item.getLang());
    holder.tv_lang_name.setText(lang.getName());
    if(lang == Lang.EN) {
      holder.iv_flg.setImageResource(R.drawable.flg_en);
    }
    else if(lang == Lang.JP){
      holder.iv_flg.setImageResource(R.drawable.flg_jp);
    }
    else if(lang == Lang.FR){
      holder.iv_flg.setImageResource(R.drawable.flg_fr);
    }
    else if(lang == Lang.KR){
      holder.iv_flg.setImageResource(R.drawable.flg_kr);
    }
    else{
      holder.iv_flg.setImageResource(R.drawable.flg_en);
    }

    if(item.getLast_learn_time() == null){
      holder.tv_last_info.setText("New!");
    }
    else {
      holder.tv_last_info.setText(PresentationUtil.Date2ShortStr(item.getLast_learn_time()) + "·" + item.getLast_learn_count());
    }
    holder.tv_need_review.setText(String.valueOf(item.getNeedreview_count()));
    holder.tv_total_count.setText(String.valueOf(item.getTotal_count()));
    holder.tv_notstart_count.setText(String.valueOf(item.getNotstart_count()));
    holder.tv_learning_count.setText(String.valueOf(item.getLearning_count()));
    holder.tv_finished_count.setText(String.valueOf(item.getFinished_count()));


    holder.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (UserWordStatListAdapter.this.onItemClickListener != null) {
          UserWordStatListAdapter.this.onItemClickListener.onItemClicked(item);
        }
      }
    });
  }

  @Override public long getItemId(int position) {
    return position;
  }

  public void setItems(List<UserWordStat> userWordStatList) {
    this.validateUsersCollection(userWordStatList);
    this.items = userWordStatList;
    this.notifyDataSetChanged();
  }

  public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
    this.onItemClickListener = onItemClickListener;
  }

  private void validateUsersCollection(Collection<UserWordStat> usersCollection) {
    if (usersCollection == null) {
      throw new IllegalArgumentException("The list cannot be null");
    }
  }

  static class ListViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.iv_flg) ImageView iv_flg;
    @Bind(R.id.tv_lang_name) TextView tv_lang_name;
    @Bind(R.id.tv_last_info) TextView tv_last_info;
    @Bind(R.id.tv_need_review) TextView tv_need_review;
    @Bind(R.id.tv_total_count) TextView tv_total_count;
    @Bind(R.id.tv_notstart_count) TextView tv_notstart_count;
    @Bind(R.id.tv_learning_count) TextView tv_learning_count;
    @Bind(R.id.tv_finished_count) TextView tv_finished_count;

    ListViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
