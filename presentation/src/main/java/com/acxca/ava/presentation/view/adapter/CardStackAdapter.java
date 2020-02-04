package com.acxca.ava.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acxca.ava.presentation.R;
import com.acxca.domain.Word;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    private List<Word> words;

    private final LayoutInflater layoutInflater;

    private Context context;

    public CardStackAdapter(Context context, List<Word> words) {
        this.context = context;
        this.words = words;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.view_word_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Word word = words.get(position);

        holder.tv_spell.setText(word.getSpell());
        holder.tv_pronounce.setText(String.format("[%s]",word.getPronounce()));
        holder.tv_meaning.setText(word.getMeaning());

        holder.rv_exps.setLayoutManager(new ListLayoutManager(context));
        WordExpListAdapter wid = new WordExpListAdapter(context);
        wid.setItem(words.get(position));
        holder.rv_exps.setAdapter(wid);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_spell) TextView tv_spell;
        @Bind(R.id.tv_pronounce) TextView tv_pronounce;
        @Bind(R.id.tv_meaning) TextView tv_meaning;
        @Bind(R.id.rv_exps) RecyclerView rv_exps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
