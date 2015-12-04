package com.jamontes79.scala.movielist.utils.json.impl


import android.util.Log
import com.jamontes79.scala.movielist.utils.MyUtils.Service
import com.jamontes79.scala.movielist.utils.json._
import com.jamontes79.scala.movielist.utils.json.entities._
import com.jamontes79.scala.movielist.utils.{ContextWrapperProvider, FileUtils}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
/**
 * Created by alberto on 2/10/15.
 */

trait ApiReads {

  implicit val moviesReads = Json.reads[ApiMovie]
  implicit val rootReads = Json.reads[ApiRoot]
  implicit val configurationReads = Json.reads[ApiConfiguration]
  implicit val rootConfigReads = Json.reads[ApiConfigRoot]
  implicit val trailerReads = Json.reads[ApiTrailer]
  implicit val rootTrailerReads = Json.reads[ApiRootTrailer]
  implicit val imagesReads = Json.reads[ApiImages]
  implicit val rootImagesReads = Json.reads[ApiRootImages]
  implicit val rootMovieDetailReads = Json.reads[ApiRootMovieDetail]
}

trait JsonServicesComponentImpl
  extends JsonServicesComponent
  with FileUtils {

  self: ContextWrapperProvider =>

  val jsonServices = new JsonServicesImpl

  class JsonServicesImpl
    extends JsonServices
    with ApiConversions
    with ApiReads {

    override def loadJson: Service[JsonRequest, JsonResponse] = request =>

      Future {

        (for {
          json <- getJson(loadJsonFile(contextProvider,0))
          apiRoot <- Try(Json.parse(json).as[ApiRoot])
        } yield apiRoot) match {
          case Success(apiRoot) => {

            JsonResponse(Some(toRoot(apiRoot)))
          }
          case Failure(ex) => {

            JsonResponse(None)
          }
        }
      }

    override def loadJsonConfig: Service[JsonRequest, JsonResponseConfig] = request =>

      Future {

        (for {
          json <- getJson(loadJsonFile(contextProvider,1))
          apiRoot <- Try(Json.parse(json).as[ApiConfigRoot])
        } yield apiRoot) match {
          case Success(apiRoot) => {

            JsonResponseConfig(Some(apiRoot))
          }
          case Failure(ex) => {

            JsonResponseConfig(None)
          }
        }
      }

    override def loadJsonTrailer: Service[JsonRequest, JsonResponseTrailer] = request =>

      Future {

        (for {
          json <- getJson(loadJsonFile(contextProvider,2))
          apiRoot <- Try(Json.parse(json).as[ApiRootTrailer])
        } yield apiRoot) match {
          case Success(apiRoot) => {

            JsonResponseTrailer(Some(apiRoot))
          }
          case Failure(ex) => {

            JsonResponseTrailer(None)
          }
        }
      }

    override def loadJsonImages: Service[JsonRequest, JsonResponseImages] = request =>

      Future {

        (for {
          json <- getJson(loadJsonFile(contextProvider,2))
          apiRoot <- Try(Json.parse(json).as[ApiRootImages])
        } yield apiRoot) match {
          case Success(apiRoot) => {

            JsonResponseImages(Some(apiRoot))
          }
          case Failure(ex) => {

            JsonResponseImages(None)
          }
        }
      }

    override def loadJsonMovieDetail: Service[JsonRequest, JsonResponseMovieDetail] = request =>

      Future {

        (for {
          json <- getJson(loadJsonFile(contextProvider,2))
          apiRoot <- Try(Json.parse(json).as[ApiRootMovieDetail])
        } yield apiRoot) match {
          case Success(apiRoot) => {
            Log.e("json","ok")
            JsonResponseMovieDetail(Some(apiRoot))
          }
          case Failure(ex) => {
            Log.e("json",ex.getMessage)
            JsonResponseMovieDetail(None)
          }
        }
      }
  }


}