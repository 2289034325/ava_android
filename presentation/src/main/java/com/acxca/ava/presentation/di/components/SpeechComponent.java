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
package com.acxca.ava.presentation.di.components;

import com.acxca.ava.presentation.di.PerActivity;
import com.acxca.ava.presentation.di.modules.ActivityModule;
import com.acxca.ava.presentation.di.modules.ReadingModule;
import com.acxca.ava.presentation.di.modules.SpeechModule;
import com.acxca.ava.presentation.view.fragment.BookMarkListFragment;
import com.acxca.ava.presentation.view.fragment.ReadingFragment;
import com.acxca.ava.presentation.view.fragment.SaveBookmarkDialogFragment;
import com.acxca.ava.presentation.view.fragment.SpeechListFragment;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SpeechModule.class})
public interface SpeechComponent extends ActivityComponent {
  void inject(SpeechListFragment speechListFragment);
}
