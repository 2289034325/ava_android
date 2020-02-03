/**
 * Copyright (C) 2015 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acxca.ava.service.api;

import android.content.Context;

import com.acxca.ava.service.ServiceConsts;
import com.acxca.ava.service.ServiceUtil;
import com.acxca.ava.service.exception.NetworkConnectionException;
import com.acxca.ava.service.net.ApiConnection;
import com.acxca.ava.service.net.Method;
import com.acxca.domain.UserWordStat;
import com.acxca.domain.Word;
import com.acxca.domain.repository.UserRepository;
import com.acxca.domain.service.DictionaryService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link UserRepository} for retrieving user data.
 */
@Singleton
public class DictionaryServiceImp implements DictionaryService {

  private final Gson gson;
  private final ServiceUtil serviceUtil;

  @Inject
  DictionaryServiceImp(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
    this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
  }

  @Override
  public Observable<List<UserWordStat>> getWordStatList() {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          Map<String,String> tokenHeader = serviceUtil.getTokenHeader();
          String responseString =  ApiConnection.create(Method.GET,ServiceConsts.API_DIC_STAT_LIST,null,tokenHeader).call();

          if (responseString != null) {
            final Type responseType = new TypeToken<List<UserWordStat>>() {}.getType();
            List<UserWordStat> m = this.gson.fromJson(responseString, responseType);
            emitter.onNext(m);
            emitter.onComplete();
          } else {
            emitter.onError(new NetworkConnectionException());
          }
        } catch (Exception e) {
          emitter.onError(new NetworkConnectionException(e.getCause()));
        }
      } else {
        emitter.onError(new NetworkConnectionException());
      }
    });
  }

  @Override
  public Observable<List<Word>> getWordList(int lang, int pageIndex,int pageSize) {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          Map<String,String> tokenHeader = serviceUtil.getTokenHeader();
          String url = String.format(ServiceConsts.API_DIC_WORD_LIST,lang,pageSize,pageIndex);
          String responseString =  ApiConnection.create(Method.GET,url,null,tokenHeader).call();

          if (responseString != null) {
            final Type responseType = new TypeToken<List<Word>>() {}.getType();
            List<Word> m = this.gson.fromJson(responseString, responseType);
            emitter.onNext(m);
            emitter.onComplete();
          } else {
            emitter.onError(new NetworkConnectionException());
          }
        } catch (Exception e) {
          emitter.onError(new NetworkConnectionException(e.getCause()));
        }
      } else {
        emitter.onError(new NetworkConnectionException());
      }
    });
  }
}
