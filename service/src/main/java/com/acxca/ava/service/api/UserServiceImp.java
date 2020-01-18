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
  private final Context context;

  /**
   * Constructs a {@link UserRepository}.
   * @param context
   */
  @Inject
  UserServiceImp( Context context) {
    this.gson = new Gson();
    this.context = context;
  }

  @Override
  public Observable<Map<String, String>> getKaptcha() {
    return Observable.create(emitter -> {
      if (isThereInternetConnection()) {
        try {
          String API_BASE_URL = "http://10.0.2.2:9000/";
          String API_URL_KAPTCHER = API_BASE_URL+"kaptcha/";
          String responseString =  ApiConnection.create(Method.GET,API_URL_KAPTCHER,null).call();

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
      if (isThereInternetConnection()) {
        try {
          String API_BASE_URL = "http://10.0.2.2:9000";
          String API_URL_KAPTCHER = String.format("%s/auth/login/%s/%s",API_BASE_URL,ticket,code);

          Map<String,String> map = new HashMap<>();
          map.put("username",username);
          map.put("password",this.getMD5(password));
          String params = this.gson.toJson(map);
          String responseString =  ApiConnection.create(Method.POST,API_URL_KAPTCHER,params).call();

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

  private String getMD5(String str) {
    try {
      // 生成一个MD5加密计算摘要
      MessageDigest md = MessageDigest.getInstance("MD5");
      // 计算md5函数
      md.update(str.getBytes());
      // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
      // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
      return new BigInteger(1, md.digest()).toString(16);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
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
