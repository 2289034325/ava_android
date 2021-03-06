/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view;

import com.acxca.ava.presentation.model.UserModel;
import com.acxca.domain.UserWordStat;
import com.acxca.domain.Word;

import java.util.Collection;
import java.util.List;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of {@link UserModel}.
 */
public interface WordStatListView extends LoadDataView {

  void renderWordStatList(List<UserWordStat> userWordStatList);

  void gotoWordsView(List<Word> wordList);

  void gotoTesting(List<Word> wordList);

  void showMenu(UserWordStat userWordStat);

}
