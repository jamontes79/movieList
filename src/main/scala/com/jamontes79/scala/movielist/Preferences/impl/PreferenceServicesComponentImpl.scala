package com.jamontes79.scala.movielist.preferences.impl

import android.content.Context
import com.jamontes79.scala.movielist.preferences.{PreferenceResponse, PreferenceRequest, PreferenceServices, PreferenceServicesComponent}
import com.jamontes79.scala.movielist.utils.ContextWrapperProvider


/**
 * Created by alberto on 6/10/15.
 */
trait PreferenceServicesComponentImpl
  extends PreferenceServicesComponent {

  self: ContextWrapperProvider =>

  val preferenceServices = new PreferenceServicesImpl

  val preferencesName = "applicationPreferences"

  class PreferenceServicesImpl
    extends PreferenceServices {

    lazy val sharedPreferences = contextProvider.application.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    def fetchIntPreference(request: PreferenceRequest[Int]): PreferenceResponse[Int] =
      PreferenceResponse(sharedPreferences.getInt(request.name, request.value))

    def saveIntPreference(request: PreferenceRequest[Int]): PreferenceResponse[Int] = {
      sharedPreferences.edit().putInt(request.name, request.value).apply()
      PreferenceResponse(request.value)
    }

    def fetchBooleanPreference(request: PreferenceRequest[Boolean]): PreferenceResponse[Boolean] =
      PreferenceResponse(sharedPreferences.getBoolean(request.name, request.value))

    def saveBooleanPreference(request: PreferenceRequest[Boolean]): PreferenceResponse[Boolean] = {
      sharedPreferences.edit().putBoolean(request.name, request.value).apply()
      PreferenceResponse(request.value)
    }

  }

}

