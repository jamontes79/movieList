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

package com.jamontes79.scala.movielist.adapters

import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import android.widget.ImageView
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.jamontes79.scala.movielist.R
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.layouts.{MoviesLayoutAdapter, ViewHolderMoviesAdapter}
import com.jamontes79.scala.movielist.utils.AsyncImageTweaks._
import macroid.ActivityContextWrapper
import macroid.FullDsl._

class SearchMovieAdapter(movies: Seq[Movie], listener: RecyclerClickListenerSearch)
    (implicit context: ActivityContextWrapper)
    extends RecyclerView.Adapter[ViewHolderMoviesAdapter] {

  val recyclerClickListener = listener

  override def onCreateViewHolder(parentViewGroup: ViewGroup, i: Int): ViewHolderMoviesAdapter = {
    val adapter = new MoviesLayoutAdapter()
    adapter.content.setOnClickListener(new OnClickListener {
      override def onClick(v: View): Unit = recyclerClickListener.onClick(adapter.cover, movies(v.getTag.asInstanceOf[Int]))
    })
    new ViewHolderMoviesAdapter(adapter)
  }

  override def getItemCount: Int = movies.size

  override def onBindViewHolder(viewHolder: ViewHolderMoviesAdapter, position: Int): Unit = {
    val pel = movies(position)
    
    viewHolder.content.setTag(position)
    
    runUi(
      (viewHolder.avatar <~
        (pel.cover map {
          srcImage(_, R.drawable.placeholder_circle,  Some(R.drawable.no_disponible))
        } getOrElse ivSrc(R.drawable.no_disponible))) ~
          (viewHolder.name <~ tvText(pel.title))
    )
  }
}

trait RecyclerClickListenerSearch {
  def onClick(iv : Option[ImageView],movie: Movie)
}