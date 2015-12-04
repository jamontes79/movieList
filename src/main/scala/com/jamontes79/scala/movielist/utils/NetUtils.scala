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

import android.util.Log

import scala.util.{Failure, Success, Try}

trait NetUtils {

  def getJson(url: String): Try[String] = {
    Try {
     val source = scala.io.Source.fromURL(url).mkString
      Log.e("json",source)
      source

    } match {
      case Success(response) => Success(response)
      case Failure(ex) =>
        ex.printStackTrace()
        Failure(ex)
    }
  }
  def getContent(url: String): Try[String] = {
    Try {
      val source = scala.io.Source.fromURL(url).mkString
      source

    } match {
      case Success(source) => Success(source)
      case Failure(ex) =>
        ex.printStackTrace()
        Failure(ex)
    }
  }

}
