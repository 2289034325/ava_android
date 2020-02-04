/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acxca.domain.interactor;

import com.acxca.domain.User;
import com.acxca.domain.Word;
import com.acxca.domain.executor.PostExecutionThread;
import com.acxca.domain.executor.ThreadExecutor;
import com.acxca.domain.service.DictionaryService;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link User}.
 */
public class GetOldWords extends UseCase<List<Word>, Map> {

  private final DictionaryService dictionaryService;

  @Inject
  GetOldWords(ThreadExecutor threadExecutor,
              PostExecutionThread postExecutionThread, DictionaryService dictionaryService) {
    super(threadExecutor, postExecutionThread);
    this.dictionaryService = dictionaryService;
  }

  @Override Observable<List<Word>> buildUseCaseObservable(Map param) {
    int lang = (int) param.get("lang");
    int count = (int)param.get("count");

    return this.dictionaryService.getOldWords(lang,count);
  }
}
