package com.jamontes79.scala.movielist.layouts

import android.support.v7.widget.Toolbar
import android.view.{ContextThemeWrapper, View}
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.jamontes79.scala.movielist.R
import com.jamontes79.scala.movielist.styles.ToolbarStyles
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, Ui}

/**
 * Created by alberto on 3/9/15.
 */
trait ToolbarLayout extends ToolbarStyles {

  var toolBar = slot[Toolbar]

  def toolBarLayout(children: Ui[View]*)(implicit context: ActivityContextWrapper): Ui[Toolbar] =
    Ui {
      val darkToolBar = getToolbarThemeDarkActionBar
      children foreach (uiView => darkToolBar.addView(uiView.get))
      toolBar = Some(darkToolBar)
      darkToolBar
    } <~ toolbarStyle(resGetDimensionPixelSize(R.dimen.height_toolbar))



  private def getToolbarThemeDarkActionBar(implicit context: ActivityContextWrapper) = {
    val contextTheme = new ContextThemeWrapper(context.getOriginal, R.style.ThemeOverlay_AppCompat_Dark_ActionBar)
    val darkToolBar = new Toolbar(contextTheme)
    darkToolBar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light)
    darkToolBar
  }

}
