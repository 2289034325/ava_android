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

import com.acxca.ava.service.ServiceConsts;
import com.acxca.ava.service.ServiceUtil;
import com.acxca.ava.service.exception.NetworkConnectionException;
import com.acxca.ava.service.net.ApiConnection;
import com.acxca.ava.service.net.Method;
import com.acxca.domain.BookMark;
import com.acxca.domain.UserWordStat;
import com.acxca.domain.Word;
import com.acxca.domain.repository.UserRepository;
import com.acxca.domain.service.DictionaryService;
import com.acxca.domain.service.ReadingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link UserRepository} for retrieving user data.
 */
@Singleton
public class ReadingServiceImp implements ReadingService {

  private final Gson gson;
  private final ServiceUtil serviceUtil;

  @Inject
  ReadingServiceImp(ServiceUtil serviceUtil) {
    this.serviceUtil = serviceUtil;
    this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
  }


  @Override
  public Observable<Word> searchWord(int lang,String form) {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          String url = String.format(ServiceConsts.API_READ_SEARCH_WORD,lang,form);
          Map<String,String> tokenHeader = serviceUtil.getTokenHeader();
          String responseString =  ApiConnection.create(Method.GET,url,null,tokenHeader).call();

          if (responseString != null) {
            final Type responseType = new TypeToken<Map>() {}.getType();
            Map m = this.gson.fromJson(responseString, responseType);
            Word w = this.gson.fromJson(this.gson.toJson(m.get("word")),Word.class);
            emitter.onNext(w);
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
  public Observable<List<BookMark>> getBookMarkList() {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          Map<String,String> tokenHeader = serviceUtil.getTokenHeader();
          String responseString =  ApiConnection.create(Method.GET,ServiceConsts.API_READ_BOOKMARK_LIST,null,tokenHeader).call();

          if (responseString != null) {
            final Type responseType = new TypeToken<List<BookMark>>() {}.getType();
            List<BookMark> bms = this.gson.fromJson(responseString, responseType);
            emitter.onNext(bms);
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
  public Observable saveBookMark(BookMark bookMark) {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          Map<String,String> tokenHeader = serviceUtil.getTokenHeader();
          Method m;
          if(bookMark.id == null || "".equals(bookMark.id)){
            bookMark.id = UUID.randomUUID().toString();
            m = Method.POST;
          }
          else{
            m = Method.PUT;
          }
          String params = gson.toJson(bookMark);
          String responseString =  ApiConnection.create(m,ServiceConsts.API_READ_BOOKMARK_SAVE_MODIFY,params,tokenHeader).call();

          if (responseString != null) {
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
  public Observable deleteBookMark(BookMark bookMark) {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          Map<String,String> tokenHeader = serviceUtil.getTokenHeader();
          String url = String.format(ServiceConsts.API_READ_BOOKMARK_SAVE_MODIFY,bookMark.getId());
          String responseString =  ApiConnection.create(Method.DELETE,url,null,tokenHeader).call();

          if (responseString != null) {
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
