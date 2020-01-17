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
import com.acxca.domain.executor.PostExecutionThread;
import com.acxca.domain.executor.ThreadExecutor;
import com.acxca.domain.repository.UserRepository;
import com.acxca.domain.service.UserService;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link User}.
 */
public class GetKaptcha extends UseCase<Map<String,String>, Void> {

  private final UserService userService;

  @Inject
  GetKaptcha(ThreadExecutor threadExecutor,
             PostExecutionThread postExecutionThread, UserService userService) {
    super(threadExecutor, postExecutionThread);
    this.userService = userService;
  }

  @Override Observable<Map<String,String>> buildUseCaseObservable(Void unused) {
    return this.userService.getKaptcha();
  }
}
