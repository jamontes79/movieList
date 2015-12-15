/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jamontes79.scala.movielist.styles

import android.graphics.Color
import android.support.v7.widget.CardView
import android.view.Gravity
import android.widget.ImageView.ScaleType
import android.widget.{FrameLayout, ImageView, LinearLayout, TextView}
import com.fortysevendeg.macroid.extras.DeviceVersion.Lollipop
import com.fortysevendeg.macroid.extras.FrameLayoutTweaks._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.LinearLayoutTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ThemeExtras
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.jamontes79.scala.movielist.R
import com.jamontes79.scala.movielist.utils.{IconTypes, PathMorphDrawable}
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, ContextWrapper, Tweak}

import scala.language.postfixOps

trait Styles {



  val contentStyle: Tweak[LinearLayout] =
    vMatchParent +
      llVertical

  val fragmentContentStyle: Tweak[FrameLayout] =
    vMatchParent

  def fabStyle(favorite: Boolean)(implicit context: ContextWrapper): Tweak[ImageView] = {
    val size = resGetDimensionPixelSize(R.dimen.size_fab_button)
    lp[FrameLayout](size, size) +
      flLayoutGravity(Gravity.END | Gravity.BOTTOM) +
    // vMargin(resGetDimensionPixelSize(R.dimen.margin_list_fab_right), resGetDimensionPixelSize(R.dimen.margin_list_fab_bottom), 0, 0) +
      vBackground(if (favorite) R.drawable.fab_button_check else R.drawable.fab_button_no_check) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab)) +
      ivSrc(new PathMorphDrawable(
        defaultIcon = if (favorite) IconTypes.CHECK else IconTypes.ADD,
        defaultStroke = resGetDimensionPixelSize(R.dimen.stroke_schedule_detail_fab),
        defaultColor = Color.WHITE
      )) +
      (Lollipop ifSupportedThen vElevation(resGetDimension(R.dimen.padding_default_extra_small)) getOrElse Tweak.blank)

  }
  def fabSaveStyle(implicit context: ContextWrapper): Tweak[ImageView] = {
    val size = resGetDimensionPixelSize(R.dimen.size_fab_button)
    lp[FrameLayout](size, size) +
      vMargin(resGetDimensionPixelSize(R.dimen.margin_schedule_detail_fab_left), resGetDimensionPixelSize(R.dimen.margin_schedule_detail_fab_top), 0, 0) +
      vBackground(R.drawable.fab_button_no_check) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab)) +
      ivSrc(new PathMorphDrawable(
        defaultIcon = IconTypes.BURGER,
        defaultStroke = resGetDimensionPixelSize(R.dimen.stroke_schedule_detail_fab),
        defaultColor = Color.WHITE
      )) +
      (Lollipop ifSupportedThen vElevation(resGetDimension(R.dimen.padding_default_extra_small)) getOrElse Tweak.blank)

  }
  def fabSearchStyle(implicit context: ContextWrapper): Tweak[ImageView] = {
    val size = resGetDimensionPixelSize(R.dimen.size_fab_button)
    lp[FrameLayout](size, size) +
      vMargin(resGetDimensionPixelSize(R.dimen.margin_list_fab_right), resGetDimensionPixelSize(R.dimen.margin_schedule_detail_fab_top), 0, 0) +
     // vBackground(R.drawable.fab_button_no_check) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_schedule_detail_fab)) +
      ivSrc(android.R.drawable.ic_menu_search) +
      (Lollipop ifSupportedThen vElevation(resGetDimension(R.dimen.padding_default_extra_small)) getOrElse Tweak.blank)

  }

}
trait AdapterStyles {

  def itemContentStyle(implicit activityContext: ActivityContextWrapper): Tweak[LinearLayout] = {


    
    vMatchParent +
      llVertical +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default)) +
      vBackground( ThemeExtras.themeGetDrawable(R.attr.selectableItemBackground).get) +
      llGravity(Gravity.CENTER_HORIZONTAL)
  }
  

def cardStyle(implicit activityContext: ActivityContextWrapper): Tweak[CardView] =
    vMatchWidth +
      (ThemeExtras.themeGetDrawable(android.R.attr.selectableItemBackground) map flForeground getOrElse Tweak.blank)

  def itemStyle: Tweak[LinearLayout] =
    llVertical +
      vMatchWidth


  def avatarStyle(implicit context: ContextWrapper): Tweak[ImageView] = {
    val avatarSize = resGetDimensionPixelSize(R.dimen.size_avatar)
    lp[LinearLayout](avatarSize, avatarSize) +
      ivScaleType(ScaleType.CENTER_CROP)
  }

  def itemNoAvatarContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchWidth +
      llVertical +
      llGravity(Gravity.CENTER_HORIZONTAL)/*+
      vPadding(resGetDimensionPixelSize(R.dimen.padding_default), 0, 0, 0)*/

  def nameItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSize(resGetInteger(R.integer.text_big)) +
      tvColorResource(R.color.text_title_default)



}
