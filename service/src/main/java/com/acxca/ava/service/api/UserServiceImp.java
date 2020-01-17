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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.acxca.ava.service.entity.UserEntity;
import com.acxca.ava.service.exception.NetworkConnectionException;
import com.acxca.ava.service.net.ApiConnection;
import com.acxca.ava.service.net.RestApi;
import com.acxca.domain.repository.UserRepository;
import com.acxca.domain.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link UserRepository} for retrieving user data.
 */
@Singleton
public class UserServiceImp implements UserService {

  private final Gson gson;
  private final Context context;

  /**
   * Constructs a {@link UserRepository}.
   * @param gson
   * @param context
   */
  @Inject
  UserServiceImp(Gson gson, Context context) {
    this.gson = gson;
    this.context = context;
  }

  @Override
  public Observable<Map<String, String>> getKaptcha() {
    return Observable.create(emitter -> {
      if (isThereInternetConnection()) {
        try {
          String API_BASE_URL = "http://localhost:9000/";
          String API_URL_KAPTCHER = API_BASE_URL+"kaptcha/";
          String responseString =  ApiConnection.createGET(API_URL_KAPTCHER).requestSyncCall();

          if (responseString != null) {
            final Type responseType = new TypeToken<Map>() {}.getType();
            Map m = this.gson.fromJson(responseString, responseType);
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

  private boolean isThereInternetConnection() {
    boolean isConnected;

    ConnectivityManager connectivityManager =
            (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

    return isConnected;
  }
}
