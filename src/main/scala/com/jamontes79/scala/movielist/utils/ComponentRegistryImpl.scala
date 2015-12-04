package com.jamontes79.scala.movielist.utils


import com.jamontes79.scala.movielist.preferences.impl.PreferenceServicesComponentImpl
import com.jamontes79.scala.movielist.utils.json.impl.JsonServicesComponentImpl
import com.jamontes79.scala.movielist.utils.net.impl.NetServicesComponentImpl


/**
 * Created by alberto on 6/10/15.
 */
trait ComponentRegistryImpl
  extends PreferenceServicesComponentImpl
  with  ContextWrapperProvider
  with NetServicesComponentImpl
  with JsonServicesComponentImpl
