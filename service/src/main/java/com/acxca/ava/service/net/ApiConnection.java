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
package com.acxca.ava.service.net;

import android.support.annotation.Nullable;

import com.acxca.ava.service.ServiceConsts;
import com.acxca.ava.service.cache.serializer.Serializer;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Api Connection class used to retrieve data from the cloud.
 * Implements {@link java.util.concurrent.Callable} so when executed asynchronously can
 * return a value.
 */
public class ApiConnection implements Callable<String> {


//  private URL url;
  private String response;
  private Request.Builder builder;
  private Request request;

//  public ApiConnection(String url) throws MalformedURLException {
//    this.url = new URL(url);
//  }

//  public static ApiConnection createGET(String url) throws MalformedURLException {
//    ApiConnection ac = new ApiConnection();
//    ac.request = new Request.Builder()
//            .url(new URL(url))
//            .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON)
//            .get()
//            .build();
//
//    return ac;
//  }

  public static ApiConnection create(Method method,String url,@Nullable String params,@Nullable Map<String,String> headers) throws MalformedURLException {
    RequestBody body = null;
    if(params != null && !params.isEmpty()) {
      MediaType JSON = MediaType.parse(ServiceConsts.API_HEADER_CONTENT_TYPE_VALUE_JSON);
      body = RequestBody.create(JSON, params);
    }

    ApiConnection ac = new ApiConnection();
    Request.Builder rb =  new Request.Builder()
            .url(new URL(url))
            .addHeader(ServiceConsts.API_HEADER_CONTENT_TYPE_LABEL, ServiceConsts.API_HEADER_CONTENT_TYPE_VALUE_JSON);

    if(headers != null) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        rb = rb.addHeader(entry.getKey(), entry.getValue());
      }
    }

    if(method == Method.GET){
      ac.request = rb.get().build();
    }
    else if(method == Method.POST){
      ac.request = rb.post(body).build();
    }
    else if(method == Method.PUT){
      ac.request = rb.put(body).build();
    }
    else if(method == Method.DELETE){
      ac.request = rb.delete(body).build();
    }

    return ac;
  }

  public ApiConnection addHeaders(Map<String,String> headers){
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      builder = builder.addHeader(entry.getKey(),entry.getValue());
      request = builder.build();
    }

    return this;
  }

  /**
   * Do a request to an api synchronously.
   * It should not be executed in the main thread of the application.
   *
   * @return A string response
   */
//  @Nullable
//  public String requestSyncCall() {
//    connectToApi();
//    return response;
//  }

//  private void connectToApi() {
//    OkHttpClient okHttpClient = this.createClient();
//    final Request request = new Request.Builder()
//        .url(this.url)
//        .addHeader(CONTENT_TYPE_LABEL, CONTENT_TYPE_VALUE_JSON)
//        .get()
//        .build();
//
//    try {
//      this.response = okHttpClient.newCall(request).execute().body().string();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

  private OkHttpClient createClient() {
    final OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
    okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);

    return okHttpClient;
  }

  @Override public String call() throws Exception {
    OkHttpClient okHttpClient = this.createClient();
    this.response = okHttpClient.newCall(request).execute().body().string();
    return this.response;
  }
}
