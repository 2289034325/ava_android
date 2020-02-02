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
package com.acxca.ava.presentation.di.modules;

import android.content.Context;

import com.acxca.ava.service.api.DictionaryServiceImp;
import com.acxca.ava.service.api.ReadingServiceImp;
import com.acxca.ava.service.api.SpeechServiceImp;
import com.acxca.ava.service.api.UserServiceImp;
import com.acxca.ava.service.api.WritingServiceImp;
import com.acxca.ava.service.cache.UserCache;
import com.acxca.ava.service.cache.UserCacheImpl;
import com.acxca.ava.service.executor.JobExecutor;
import com.acxca.ava.service.api.UserDataRepository;
import com.acxca.domain.executor.PostExecutionThread;
import com.acxca.domain.executor.ThreadExecutor;
import com.acxca.domain.repository.UserRepository;
import com.acxca.ava.presentation.AndroidApplication;
import com.acxca.ava.presentation.UIThread;
import com.acxca.domain.service.DictionaryService;
import com.acxca.domain.service.ReadingService;
import com.acxca.domain.service.SpeechService;
import com.acxca.domain.service.UserService;
import com.acxca.domain.service.WritingService;

import java.util.Map;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {
  private final AndroidApplication application;
  private final Map<String,Object> sharedBag;

  public ApplicationModule(AndroidApplication application) {
    this.application = application;
    this.sharedBag = application.sharedBag;
  }

  @Provides @Singleton Map<String,Object> getSharedBag(){
    return this.sharedBag;
  }

  @Provides @Singleton Context provideApplicationContext() {
    return this.application;
  }

  @Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }

  @Provides @Singleton UserCache provideUserCache(UserCacheImpl userCache) {
    return userCache;
  }

  @Provides @Singleton UserRepository provideUserRepository(UserDataRepository userDataRepository) {
    return userDataRepository;
  }

  @Provides @Singleton UserService provideUserService(UserServiceImp userServiceImp) {
    return userServiceImp;
  }

  @Provides
  @Singleton
  DictionaryService provideDictionaryService(DictionaryServiceImp dictionaryServiceImp) {
    return dictionaryServiceImp;
  }

  @Provides
  @Singleton
  ReadingService provideReadingService(ReadingServiceImp readingServiceImp) {
    return readingServiceImp;
  }

  @Provides
  @Singleton
  SpeechService provideSpeechService(SpeechServiceImp speechServiceImp) {
    return speechServiceImp;
  }

  @Provides
  @Singleton
  WritingService provideWritingService(WritingServiceImp writingServiceImp) {
    return writingServiceImp;
  }
}
