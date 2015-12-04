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

package com.jamontes79.scala.movielist.utils

import java.io.File

import android.widget.ImageView
import com.fortysevendeg.macroid.extras.DeviceVersion._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.squareup.picasso.Picasso
import macroid.{ActivityContextWrapper, Tweak}

import scala.language.postfixOps

object AsyncImageTweaks {
  type W = ImageView

  def roundedImage(url: String,
        placeHolder: Int,
        size: Int,
        error: Option[Int] = None)(implicit context: ActivityContextWrapper) = CurrentVersion match {
    case sdk if sdk >= Lollipop =>
      srcImage(url, placeHolder, error) + vCircleOutlineProvider(0)
    case _ =>
      roundedImageTweak(url, placeHolder, size, error)
  }
  def roundedImageFile(url: String,
                   placeHolder: Int,
                   size: Int,
                   error: Option[Int] = None)(implicit context: ActivityContextWrapper) = CurrentVersion match {
    case sdk if sdk >= Lollipop =>
      srcImageFile(url, placeHolder, error) + vCircleOutlineProvider(0)
    case _ =>
      roundedImageTweak(url, placeHolder, size, error)
  }
  private def roundedImageTweak(
      url: String,
      placeHolder: Int,
      size: Int,
      error: Option[Int] = None
      )(implicit activityContext: ActivityContextWrapper): Tweak[W] = Tweak[W](
    imageView => {
      val request = Picasso.`with`(activityContext.getOriginal)
          .load(url)
          .transform(new CircularTransformation(size))
          .placeholder(placeHolder)
      error map request.error
      request.into(imageView)
    }
  )

  def srcImage(
      url: String,
      placeHolder: Int,
      error: Option[Int] = None
      )(implicit context: ActivityContextWrapper): Tweak[W] = Tweak[W](
    imageView => {
      val request =
        Picasso.`with`(context.getOriginal)
          .load(url)
          .placeholder(placeHolder)
      error map request.error
      request.into(imageView)
    }
  )
  def srcImageFile(
                url: String,
                placeHolder: Int,
                error: Option[Int] = None
                )(implicit context: ActivityContextWrapper): Tweak[W] = Tweak[W](

    imageView => {

      val request =
        Picasso.`with`(context.getOriginal)
          .load(new File(url))
          .placeholder(placeHolder)
      error map request.error
      request.into(imageView)
    }
  )
  def srcImage(url: String)(implicit context: ActivityContextWrapper): Tweak[W] = Tweak[W](
    imageView => {
      Picasso.`with`(context.getOriginal)
          .load(url)
          .into(imageView)
    }
  )
}

