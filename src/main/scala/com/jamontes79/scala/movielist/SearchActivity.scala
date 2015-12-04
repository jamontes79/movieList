package com.jamontes79.scala.movielist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.fortysevendeg.macroid.extras.FragmentExtras._
import com.jamontes79.scala.movielist.layouts.SearchLayout
import com.jamontes79.scala.movielist.utils.MyUtils
import macroid.FullDsl._
import macroid._

import scala.language.postfixOps


/**
 * Created by alberto on 2/9/15.
 */
class SearchActivity  extends AppCompatActivity
with Contexts[FragmentActivity]
with SearchLayout
with IdGeneration
with MyUtils {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)
    checkDataBase(this)

    toolBar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(false)
    getSupportActionBar.setHomeButtonEnabled(true)

    if (savedInstanceState == null) {
      runUi(
        replaceFragment(
          builder = f[SearchFragment],
          id = Id.searchFragment,
          tag = Some(Tag.searchFragment)))
    }
  }

  override def onNewIntent(intent: Intent): Unit = {
    super.onNewIntent(intent)
    setIntent(intent)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    findFragmentByTag[SearchFragment](Tag.searchFragment) match {
      case Some(f) => f.asInstanceOf[SearchFragment].onActivityResult(requestCode, resultCode, data)
    }
  }


}
