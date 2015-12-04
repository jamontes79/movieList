package com.jamontes79.scala.movielist.utils

/**
 * Created by alberto on 29/9/15.
 */
case class InvalidJsonException()
  extends RuntimeException("Json load failed")
