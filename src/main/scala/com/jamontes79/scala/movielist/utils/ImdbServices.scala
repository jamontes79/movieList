package com.jamontes79.scala.movielist.utils


import java.util.Locale

import android.util.Log
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.preferences.PreferenceServicesComponent
import com.jamontes79.scala.movielist.utils.json.entities.{ApiRootMovieDetail, ApiConfiguration}
import com.jamontes79.scala.movielist.utils.json.{JsonRequest, JsonServicesComponent}
import com.jamontes79.scala.movielist.utils.net.{NetRequest, NetServicesComponent}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
/**
 * Created by alberto on 6/10/15.
 */
trait ImdbServices {
  self : PreferenceServicesComponent
    with JsonServicesComponent
    with NetServicesComponent
    with ContextWrapperProvider =>

  val API_KEY = "f51d8f513700b6d7a7ae965dae466685"
  val IMDB_BASE_URL_SEARCH_LIST = "http://api.themoviedb.org/3/search/movie?api_key=#API#&query=#TITLE_SEARCH#&language=#LANGUAGE#"
  val IMDB_BASE_URL_CONFIGURATION = "http://api.themoviedb.org/3/configuration?api_key=#API#"
  val IMDB_BASE_URL_TRAILER = "http://api.themoviedb.org/3/movie/#IMDB_ID#/trailers?api_key=#API#"
  val IMDB_SUFIX_TRAILER_LANG = "&language="
  val IMDB_BASE_URL_SEARCH_FILM = "http://api.themoviedb.org/3/movie/#IMDB_ID#?api_key=#API#&language=#LANGUAGE#"
  val IMDB_BASE_URL_IMAGES = "http://api.themoviedb.org/3/movie/#IMDB_ID#/images?api_key=#API#"

  def scape(value : String) : String = {
        var scapedText = ""
        scapedText = value.replaceAll("Á", "A").replaceAll("É", "E").replaceAll("Í", "I").replaceAll("Ó", "O").replaceAll("Ú", "U")
    scapedText
  }
  def cleanForSearch(text : String) : String = {
        var cleanedText = ""
        cleanedText = scape(text.toUpperCase())
        cleanedText = cleanedText.replaceAll("[^A-Z0-9]", " ")
        cleanedText = cleanedText.replaceAll("\\s\\s+", "%20")
        cleanedText
    }

  def searchPeliculas(titulo: String = "",forceDownload: Boolean = false): Future[Seq[Movie]] = {
    var url = ""
    url = IMDB_BASE_URL_SEARCH_LIST.replaceAll("#LANGUAGE#", Locale.getDefault().getLanguage()).replaceAll("#API#", API_KEY).replaceAll("#TITLE_SEARCH#", cleanForSearch(titulo))
    url = url.replaceAll(" ", "%20")

    for {
      _ <- netServices.saveJsonInLocal(NetRequest(url,forceDownload))
      jsonResponse <- jsonServices.loadJson(JsonRequest())
      root <- Future.successful(jsonResponse.apiResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      root.movies
    }
  }

  def searchTrailer(imdb: String = "", searchLanguage : Boolean): Future[String] = {
    var url = ""
    url = IMDB_BASE_URL_TRAILER.replaceAll("#LANGUAGE#", Locale.getDefault().getLanguage()).replaceAll("#API#", API_KEY).replaceAll("#IMDB_ID#", imdb)
    url = url.replaceAll(" ", "%20")

    val urlYoutube = "http://www.youtube.com/embed/"
    if (searchLanguage)  url += IMDB_SUFIX_TRAILER_LANG + Locale.getDefault().getLanguage()
    for {
      _ <- netServices.saveJsonGenericInLocal(NetRequest(url,true))
      jsonResponse <- jsonServices.loadJsonTrailer(JsonRequest())
      trailerRoot <- Future.successful(jsonResponse.trailerResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      urlYoutube + trailerRoot.youtube.lift(0).get.source
    }
  }

  def reloadCovers(imdb: String = ""): Future[String] = {
    var url = ""
    url = IMDB_BASE_URL_IMAGES.replaceAll("#LANGUAGE#", Locale.getDefault().getLanguage()).replaceAll("#API#", API_KEY).replaceAll("#IMDB_ID#", imdb)
    url = url.replaceAll(" ", "%20")
    url += IMDB_SUFIX_TRAILER_LANG + Locale.getDefault().getLanguage()

    for {
      _ <- netServices.saveJsonGenericInLocal(NetRequest(url,true))
      jsonResponse <- jsonServices.loadJsonImages(JsonRequest())
      imageRoot <- Future.successful(jsonResponse.imageResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      imageRoot.posters.lift(0).get.file_path
    }
  }

  def searchFilm(imdb: String = ""): Future[ApiRootMovieDetail] = {
    var url = ""
    url = IMDB_BASE_URL_SEARCH_FILM.replaceAll("#LANGUAGE#", Locale.getDefault().getLanguage()).replaceAll("#API#", API_KEY).replaceAll("#IMDB_ID#", imdb)
    url = url.replaceAll(" ", "%20")

    Log.e("url",url)
    for {
      _ <- netServices.saveJsonGenericInLocal(NetRequest(url,true))
      jsonResponse <- jsonServices.loadJsonMovieDetail(JsonRequest())
      filmRoot <- Future.successful(jsonResponse.filmResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      filmRoot
    }
  }

  def loadConfiguration(forceDownload: Boolean = false): Future[ApiConfiguration] = {

    var url = ""
    url = IMDB_BASE_URL_CONFIGURATION.replaceAll("#API#", API_KEY)
    url = url.replaceAll(" ", "%20")

    for {
      _ <- netServices.loadConfiguration(NetRequest(url,forceDownload))
      jsonResponse <- jsonServices.loadJsonConfig(JsonRequest())
      configRoot <- Future.successful(jsonResponse.configResponse.getOrElse(throw InvalidJsonException()))
    } yield {
      configRoot.images
    }

  }
}
