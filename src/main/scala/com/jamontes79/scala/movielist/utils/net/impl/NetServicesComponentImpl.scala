package com.jamontes79.scala.movielist.utils.net.impl

import java.io.File

import android.util.Log
import com.jamontes79.scala.movielist.R
import com.jamontes79.scala.movielist.utils.MyUtils._
import com.jamontes79.scala.movielist.utils.net._
import com.jamontes79.scala.movielist.utils.{ContextWrapperProvider, FileUtils, NetUtils}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}
/**
 * Created by alberto on 29/9/15.
 */
trait NetServicesComponentImpl
  extends NetServicesComponent
  with FileUtils
  with NetUtils {

  self: ContextWrapperProvider =>

  val netServices = new NetServicesImpl

  def loadJsonFileName: String =
    contextProvider.application.getString(R.string.url_json_peliculas)

  class NetServicesImpl
    extends NetServices {


    override def saveJsonGenericInLocal: Service[NetRequest, NetResponse] = request =>
      Future {
        val file = loadJsonFile(contextProvider,2)


          val result = getJson(request.url) map (writeJsonFile(file, _))

          result match {
            case Success(true) => NetResponse(success = true, downloaded = true)
            case _ => NetResponse(success = false, downloaded = false)
          }

      }
    override def saveJsonInLocal: Service[NetRequest, NetResponse] = request =>
      Future {
        val file = loadJsonFile(contextProvider,0)

        if (request.forceDownload || !file.exists()) {
          val result = getJson(request.url) map (writeJsonFile(file, _))

          result match {
            case Success(true) => NetResponse(success = true, downloaded = true)
            case _ => NetResponse(success = false, downloaded = false)
          }
        } else {
          NetResponse(success = true, downloaded = false)
        }
      }
    override def loadConfiguration: Service[NetRequest, NetResponse] = request =>
      Future {
        val file = loadJsonFile(contextProvider,1)

        if (request.forceDownload || !file.exists()) {
          val result = getJson(request.url) map (writeJsonFile(file, _))

          result match {
            case Success(true) => NetResponse(success = true, downloaded = true)
            case _ => NetResponse(success = false, downloaded = false)
          }
        } else {
          NetResponse(success = true, downloaded = false)
        }
      }
    def writeJsonFile(file: File, jsonContent: String): Boolean = {
      if (file.exists()) file.delete()
      Try {
        Log.e("json",jsonContent)
        writeText(file, jsonContent)
      } match {
        case Success(response) => true
        case Failure(ex) => false
      }
    }

  }

}
