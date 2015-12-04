package com.jamontes79.scala.movielist.utils.json



import com.jamontes79.scala.movielist.entities.Root
import com.jamontes79.scala.movielist.utils.json.entities.{ApiRootMovieDetail, ApiRootImages, ApiRootTrailer, ApiConfigRoot}

/**
 * Created by alberto on 29/9/15.
 */
case class JsonRequest()

case class JsonResponse(apiResponse: Option[Root])
case class JsonResponseConfig(configResponse: Option[ApiConfigRoot])
case class JsonResponseTrailer(trailerResponse: Option[ApiRootTrailer])
case class JsonResponseImages(imageResponse: Option[ApiRootImages])
case class JsonResponseMovieDetail(filmResponse: Option[ApiRootMovieDetail])
