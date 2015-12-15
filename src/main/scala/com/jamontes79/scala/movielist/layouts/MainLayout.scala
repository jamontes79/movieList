/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jamontes79.scala.movielist.layouts

import android.support.v7.widget.{CardView, RecyclerView}
import android.widget._
import com.jamontes79.scala.movielist.styles.{AdapterStyles, Styles}
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, IdGeneration}

class MoviesLayoutAdapter(implicit context: ActivityContextWrapper)
    extends AdapterStyles {

  var cover = slot[ImageView]
  var name = slot[TextView]
  val content = layout

  private def layout(implicit context: ActivityContextWrapper) = getUi(
  l[CardView](
    l[LinearLayout](
      w[ImageView] <~ wire(cover) <~ avatarStyle,
      l[LinearLayout](
        w[TextView] <~ wire(name) <~ nameItemStyle
      ) <~ itemNoAvatarContentStyle
    ) <~ itemStyle
    )<~ cardStyle
  )
}

class ViewHolderMoviesAdapter(adapter: MoviesLayoutAdapter)(implicit context: ActivityContextWrapper)
    extends RecyclerView.ViewHolder(adapter.content) {

  val content = adapter.content
  val avatar = adapter.cover
  val name = adapter.name


}
trait MainLayout
  extends ToolbarLayout
  with IdGeneration
  with Styles {

  var fragmentContent = slot[FrameLayout]

  def layout(implicit context: ActivityContextWrapper) = {
    getUi(

        l[LinearLayout](
          toolBarLayout(),
          l[FrameLayout]() <~ wire(fragmentContent) <~ id(Id.mainFragment) <~ fragmentContentStyle
        ) <~ contentStyle
    )
  }

}
