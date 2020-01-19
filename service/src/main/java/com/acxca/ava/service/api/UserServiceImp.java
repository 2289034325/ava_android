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

import com.acxca.ava.service.ServiceConsts;
import com.acxca.ava.service.ServiceUtil;
import com.acxca.ava.service.entity.UserEntity;
import com.acxca.ava.service.exception.NetworkConnectionException;
import com.acxca.ava.service.net.ApiConnection;
import com.acxca.ava.service.net.Method;
import com.acxca.ava.service.net.RestApi;
import com.acxca.domain.repository.UserRepository;
import com.acxca.domain.service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.MessageDigest;
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
  private final ServiceUtil serviceUtil;

  @Inject
  UserServiceImp(ServiceUtil serviceUtil) {
    this.gson = new Gson();
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Observable<Map<String, String>> getKaptcha() {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {

          String responseString =  ApiConnection.create(Method.GET,ServiceConsts.API_AUTH_KAPTCHA,null,null).call();

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

  @Override
  public Observable<String> login(String username,String password,String code,String ticket)
  {
    return Observable.create(emitter -> {
      if (serviceUtil.isThereInternetConnection()) {
        try {
          Map<String,String> map = new HashMap<>();
          map.put("username",username);
          map.put("password",serviceUtil.getMD5(password));
          String params = this.gson.toJson(map);
          String url = String.format(ServiceConsts.API_AUTH_LOGIN,ticket,code);
          String responseString =  ApiConnection.create(Method.POST, url,params,null).call();

          if (responseString != null) {
            String m = this.gson.fromJson(responseString, String.class);
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
