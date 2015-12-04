package com.jamontes79.scala.movielist

/*
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices, LineItemDecorator}
*/

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.ImageView
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.jamontes79.scala.movielist.adapters.{RecyclerClickListenerSearch, SearchMovieAdapter}
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.layouts.ListLayout
import com.jamontes79.scala.movielist.utils.json.entities.ApiConfiguration
import com.jamontes79.scala.movielist.utils.{ComponentRegistryImpl, ImdbServices, MyUtils}
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by alberto on 2/9/15.
 */
class SearchFragment extends Fragment
with Contexts[Fragment]
with ImdbServices
with ComponentRegistryImpl
with ListLayout {

  var titleSearch = ""

   def loadMovies(forceDownload: Boolean = false): Future[Seq[Movie]] = {
     loadConfiguration(false).onComplete{
       case Success(c) => MyUtils.config = c
       case Failure(t) => MyUtils.config = new ApiConfiguration

     }
     searchPeliculas(titulo = titleSearch,forceDownload = forceDownload)
  }

  lazy val contextProvider: ContextWrapper = fragmentContextWrapper

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    contentSearch
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)
    val maybeTitle = Option(getActivity.getIntent.getExtras) map {
      extras =>

        val titles : Option[String] = if (extras.containsKey("titleToSearch"))
          Some(extras.getString("titleToSearch"))
        else None
        titles

    } getOrElse None
    titleSearch = maybeTitle.get
    runUi(
      (recyclerView
        <~ rvLayoutManager(new GridLayoutManager(fragmentContextWrapper.application,2))) ~
        searchMovies(true) )
  }

  def searchMovies(forceDownload: Boolean = false): Ui[_] = {
    loadMovies(forceDownload) mapUi {
      movies => reloadList(movies)
    } recoverUi {
      case _ => failed()
    }
    loading()
  }

  def reloadList(movies: Seq[Movie]): Ui[_] = {

    movies.length match {
      case 0 =>  empty()
      case _ =>
        val moviesAdapter = new SearchMovieAdapter(movies, new RecyclerClickListenerSearch {
          override def onClick(iv : Option[ImageView],movie: Movie): Unit = {
            loadConfiguration(false).onComplete{
              case Success(c) => MyUtils.config = c
              case Failure(t) => MyUtils.config = new ApiConfiguration

            }
            searchFilm(movie.imdb.getOrElse("")) map { film =>

              val intent = getActivity().getIntent()
              intent.putExtra("imdbId", movie.imdb.getOrElse(""))
              intent.putExtra("titulo", movie.title)
              intent.putExtra("sinopsis", movie.sinopsis.getOrElse(""))
              intent.putExtra("caratula", movie.cover.getOrElse(""))
              intent.putExtra("thumb", movie.thumb.getOrElse(""))
              intent.putExtra("duracion", film.runtime.toString)
              intent.putExtra("fullimdbId", film.imdb_id)
              intent.putExtra("puntuacion", film.vote_average.toFloat)
              getActivity().setResult(Activity.RESULT_OK, intent)
              getActivity().finish()

            }

          }
        })
        adapter(moviesAdapter)
    }
  }

}