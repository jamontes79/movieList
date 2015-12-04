package com.jamontes79.scala.movielist.layouts

import android.widget.{LinearLayout, FrameLayout}
import com.jamontes79.scala.movielist.styles.Styles
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, IdGeneration}

/**
 * Created by alberto on 30/10/15.
 */
trait ImageLayout
  extends ToolbarLayout
  with IdGeneration
  with Styles {
 var fragmentContent = slot[FrameLayout]

  def layout(implicit context: ActivityContextWrapper) = {
    getUi(

      l[LinearLayout](
          toolBarLayout(),
          l[FrameLayout]() <~ wire(fragmentContent) <~ id(Id.imageFragment) <~ fragmentContentStyle
        ) <~ contentStyle
    )
  }

}
