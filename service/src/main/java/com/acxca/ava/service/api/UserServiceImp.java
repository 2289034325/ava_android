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

import com.acxca.ava.service.api.datasource.UserDataStoreFactory;
import com.acxca.ava.service.entity.mapper.UserEntityDataMapper;
import com.acxca.ava.service.net.RestApi;
import com.acxca.domain.repository.UserRepository;
import com.acxca.domain.service.UserService;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link UserRepository} for retrieving user data.
 */
@Singleton
public class UserServiceImp implements UserService {

  private final RestApi restApi;

  /**
   * Constructs a {@link UserRepository}.
   * @param restApi
   */
  @Inject
  UserServiceImp(RestApi restApi) {
    this.restApi = restApi;
  }

  @Override
  public Observable<Map<String, String>> captcher() {
    return null;
  }
}
