package com.jamontes79.scala.movielist


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.Menu
import com.fortysevendeg.macroid.extras.FragmentExtras._
import com.jamontes79.scala.movielist.layouts.MainLayout
import com.jamontes79.scala.movielist.utils.MyUtils
import macroid.FullDsl._
import macroid._
import org.codechimp.apprater.AppRater

import scala.language.postfixOps








/**
 * Created by alberto on 2/9/15.
 */
class ListActivity  extends AppCompatActivity
with Contexts[FragmentActivity]
with MainLayout
with IdGeneration
with MyUtils
with OnQueryTextListener {

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
          builder = f[ListFragment],
          id = Id.mainFragment,
          tag = Some(Tag.mainFragment)))
    }
    AppRater.app_launched(this)
  }

  override def onNewIntent(intent: Intent): Unit = {
    super.onNewIntent(intent)
    setIntent(intent)
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)

    findFragmentByTag[ListFragment](Tag.mainFragment) match {
      case Some(f) => f.asInstanceOf[ListFragment].onActivityResult(requestCode, resultCode, data)
    }
  }

override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.menu_list, menu)
    val searchView : SearchView = MenuItemCompat.getActionView( menu.findItem(R.id.action_search)).asInstanceOf[SearchView]
    searchView.setOnQueryTextListener(this)
    return true
  }

  override def onQueryTextChange(s: String): Boolean = {

    findFragmentByTag[ListFragment](Tag.mainFragment) match {
      case Some(f) => f.asInstanceOf[ListFragment].search(s)
    }
    true
  }

  override def onQueryTextSubmit(s: String): Boolean = {
    false
  }
}
