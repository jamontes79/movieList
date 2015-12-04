package com.jamontes79.scala.movielist.utils

import macroid.ContextWrapper

/**
 * Created by alberto on 29/9/15.
 */

trait ContextWrapperProvider {

  implicit val contextProvider : ContextWrapper

}