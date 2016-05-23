/*
 * Copyright 2016 Tom Hall.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.solera.defragsample;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.solera.defrag.ViewStack;
import rx.Observable;

public class TotalCostView extends FrameLayout implements TotalCostPresenter.View {
  private final TotalCostPresenter mPresenter = new TotalCostPresenter();
  @Bind(R.id.button) FloatingActionButton mFloatingActionButton;
  @Bind(R.id.edittext) EditText mEditText;

  public TotalCostView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.bind(this);

  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mPresenter.takeView(this);
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mPresenter.dropView();
  }

  @NonNull @Override public Observable<CharSequence> onTotalCostChanged() {
    return RxTextView.textChanges(mEditText);
  }

  @NonNull @Override public Observable<?> onSubmit() {
    return Observable.merge(RxView.clicks(mFloatingActionButton),
        RxTextView.editorActions(mEditText));
  }

  @Override public void enableSubmit(boolean enable) {
    mFloatingActionButton.setEnabled(enable);
    final float scaleTo = enable ? 1.0f : 0.0f;
    mFloatingActionButton.animate().scaleX(scaleTo).scaleY(scaleTo);
  }

  @Override public void showTotalPeople(int totalCost) {
    TotalPeoplePresenter.push(ViewStack.get(this),totalCost);
  }
}
