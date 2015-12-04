package com.jamontes79.scala.movielist.preferences



/**
 * Created by alberto on 29/9/15.
 */

trait PreferenceServices {

  def fetchIntPreference(request: PreferenceRequest[Int]): PreferenceResponse[Int]

  def saveIntPreference(request: PreferenceRequest[Int]): PreferenceResponse[Int]

  def fetchBooleanPreference(request: PreferenceRequest[Boolean]): PreferenceResponse[Boolean]

  def saveBooleanPreference(request: PreferenceRequest[Boolean]): PreferenceResponse[Boolean]
}

trait PreferenceServicesComponent {
  val preferenceServices: PreferenceServices
}
