/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jamontes79.scala.movielist.styles

import android.support.v7.widget.{RecyclerView, Toolbar}
import android.view.ViewGroup.LayoutParams._
import android.view.{Gravity, View}
import android.widget._
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.jamontes79.scala.movielist.R
import macroid.FullDsl._
import macroid.{ContextWrapper, Tweak}

import scala.language.postfixOps

trait ToolbarStyles {

  def toolbarStyle(height: Int)(implicit context: ContextWrapper): Tweak[Toolbar] =
    vContentSizeMatchWidth(height) +
      vBackground(R.color.primary)

}

trait PlaceHolderStyles {

  val placeholderContentStyle: Tweak[LinearLayout] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER) +
      llGravity(Gravity.CENTER_HORIZONTAL) +
      llVertical +
      vGone

  val placeholderImageStyle: Tweak[ImageView] =
    vWrapContent

  val placeholderImageDetailStyle: Tweak[ImageView] =
      layoutParams[LinearLayout](MATCH_PARENT, MATCH_PARENT) +
      flLayoutGravity(Gravity.CENTER)

  val placeholderImageViewStyle: Tweak[ImageView] =
    layoutParams[LinearLayout](MATCH_PARENT, MATCH_PARENT)

  def vMatchParentWrapContext(implicit context: ContextWrapper): Tweak[View] =
    layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT)

  def placeholderMessageStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvGravity(Gravity.CENTER) +
      tvColorResource(R.color.text_error_message) +
      tvSize(resGetInteger(R.integer.text_big)) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default_big))

  def placeholderButtonStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      vMinWidth(resGetDimensionPixelSize(R.dimen.width_button)) +
      tvText(R.string.reload) +
      tvColorResource(R.color.text_error_button) +
      vBackground(R.drawable.background_error_button) +
      tvAllCaps +
      tvSize(resGetInteger(R.integer.text_medium)) +
      tvGravity(Gravity.CENTER)

  def placeholderSearchButtonStyle(implicit context: ContextWrapper): Tweak[ImageButton] =
      //vMinWidth(resGetDimensionPixelSize(R.dimen.width_button)) +
    vWrapContent +
      vBackground(android.R.drawable.ic_menu_search)//+
     // tvGravity(Gravity.CENTER)

  def placeholderSpinnerStyle(implicit context: ContextWrapper): Tweak[Spinner] =
    layoutParams[LinearLayout](MATCH_PARENT, WRAP_CONTENT)

}



trait ListStyles {


  def rootStyle(implicit context: ContextWrapper): Tweak[FrameLayout] = vMatchParent

  val recyclerViewStyle: Tweak[RecyclerView] =
    vMatchParent +
      rvNoFixedSize

  val progressBarStyle: Tweak[ProgressBar] =
    vWrapContent +
      flLayoutGravity(Gravity.CENTER)
}

