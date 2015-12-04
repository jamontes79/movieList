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

package com.jamontes79.scala.movielist.layouts

import android.support.v7.widget.CardView
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.jamontes79.scala.movielist.R
import com.jamontes79.scala.movielist.styles.{PlaceHolderStyles, Styles}
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, Ui}

trait PlaceHolderLayout
  extends PlaceHolderStyles
with Styles{


  var reloadButton = slot[Button]
  var frameView = slot[FrameLayout]
  var cardView = slot[CardView]
  var image = slot[ImageView]
  var imageOnView = slot[ImageView]
  var text = slot[TextView]

  def placeholderImage(implicit context: ActivityContextWrapper) = {
    // l[ScrollView](
    l[FrameLayout](
        w[ImageView] <~ placeholderImageViewStyle <~ wire(imageOnView)
          <~ ivSrc(R.drawable.no_disponible)
          <~ ivScaleType(ScaleType.FIT_XY)

      )<~ layoutParams[LinearLayout](MATCH_PARENT, MATCH_PARENT)
    //) <~ layoutParams[LinearLayout](MATCH_PARENT, MATCH_PARENT)<~ vBackgroundColor(R.color.accent)
  }


  def placeholderList(implicit context: ActivityContextWrapper) = {
    l[LinearLayout](
      w[ImageView] <~ placeholderImageStyle <~ wire(image),
      w[TextView] <~ placeholderMessageStyle <~ wire(text),
      w[Button] <~ placeholderButtonStyle <~ wire(reloadButton)
    ) <~ placeholderContentStyle
  }

  def loadFailed(): Ui[_] = load(R.string.generalMessageError, R.drawable.placeholder_error)

  def loadEmpty(): Ui[_] = load(R.string.generalMessageEmpty, R.drawable.placeholder_general)


  private def load(messageRes: Int, imageRes: Int, showButton: Boolean = true): Ui[_] =
    (text <~ tvText(messageRes)) ~
      (image <~ ivSrc(imageRes)) ~
      (reloadButton <~ (if (showButton) vVisible else vGone))

}
