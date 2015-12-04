package com.jamontes79.scala.movielist.preferences

/**
 * Created by alberto on 29/9/15.
 */
case class PreferenceRequest[T](name: String, value: T)

case class PreferenceResponse[T](value: T)
