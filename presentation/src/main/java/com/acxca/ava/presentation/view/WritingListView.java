/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.acxca.ava.presentation.view;

import com.acxca.ava.presentation.model.UserModel;
import com.acxca.domain.Speech;
import com.acxca.domain.WritingArticle;

import java.util.List;

/**
 * Interface representing a View in a model view presenter (MVP) pattern.
 * In this case is used as a view representing a list of {@link UserModel}.
 */
public interface WritingListView extends LoadDataView {

  void renderArticleList(List<WritingArticle> articleList);

  void openArticle(WritingArticle article);
}
