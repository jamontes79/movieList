package com.jamontes79.scala.movielist
/*
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices, LineItemDecorator}
*/

import java.io.File
import java.net.URL

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.ImageView
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.jamontes79.scala.movielist.adapters.{MoviesAdapter, RecyclerClickListener}
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.layouts.ListLayout
import com.jamontes79.scala.movielist.utils.{ComponentRegistryImpl, ImdbServices, MyUtils}
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Ui}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.sys.process._


/**
 * Created by alberto on 2/9/15.
 */
class ListFragment extends Fragment
with Contexts[Fragment]
with ComponentRegistryImpl
with ImdbServices
with ListLayout {
  var moviesAdapter : MoviesAdapter = null
  var movieList: Array[Movie] = {
    Movie.loadAllMovies
  }

  def loadAllMovies(forceDownload: Boolean = false): Future[Array[Movie]] = {
    loadConfiguration(false)
    Future.successful(movieList)


  }

  lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    runUi(
      (recyclerView
        <~ rvLayoutManager(new GridLayoutManager(fragmentContextWrapper.application, 2))
        ) ~
        loadMovies() ~
        (reloadButton <~ On.click(
          Ui {
            DetailActivity.navigate(getActivity, reloadButton.get, new Movie, MyUtils.NEW_REQCODE)
          }
        )))
  }

  def loadMovies(forceDownload: Boolean = false): Ui[_] = { 
    loadAllMovies(forceDownload) mapUi {
      movies => reloadList(movies)
    } recoverUi {
      case _ => failed()
    }
    loaded()
  }
  
  def reloadList(movies: Array[Movie]): Ui[_] = {
    movies.length match {
      case 0 => empty()
      case _ =>{
        val moviesTmp : ArrayBuffer[Movie] = new ArrayBuffer[Movie]()
        movies.copyToBuffer(moviesTmp)
        moviesAdapter = new MoviesAdapter(moviesTmp, new RecyclerClickListener {
          override def onClick(iv: Option[ImageView], pelicula: Option[Movie]): Unit = {
            ViewActivity.navigate(getActivity, iv.get, pelicula.get, MyUtils.MODIFY_REQCODE)

          }
        },movies)
        adapter(moviesAdapter)
      }

    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    if (resultCode == Activity.RESULT_OK) {
      movieList = Movie.loadAllMovies
      loadMovies()
    }
    super.onActivityResult(requestCode, resultCode, data)

  }
  def downloadFile(url: Option[String], dest: String): Future[Boolean] = Future {
    try {
      if (url.getOrElse("").startsWith("http://")) {
        new URL(url.get) #> new File(dest) !!
      }

      true
    } catch {
      case e: java.io.IOException => "error occured"
        false
    }
  }


  def search(s : String): Unit = {
    if (movieList.length > 0) {
      var moviesTmp: ArrayBuffer[Movie] = new ArrayBuffer[Movie]()
      movieList.copyToBuffer(moviesTmp)
      if (s.trim.length > 0) {


        moviesTmp = moviesTmp.filter(x => x.title.toUpperCase.contains(s.toUpperCase))

        moviesAdapter.animateTo(moviesTmp)
        if (recyclerView.get.getAdapter.getItemCount > 0) {
          recyclerView.get.scrollToPosition(0)
        }
      }
      else {
        moviesAdapter.animateTo(moviesTmp)
        recyclerView.get.scrollToPosition(0)
      }
    }


  }


}