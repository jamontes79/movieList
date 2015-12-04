package com.jamontes79.scala.movielist.utils.json

import com.jamontes79.scala.movielist.utils.MyUtils.Service


/**
 * Created by alberto on 29/9/15.
 */
trait JsonServices {
  def loadJson: Service[JsonRequest, JsonResponse]
  def loadJsonConfig: Service[JsonRequest, JsonResponseConfig]
  def loadJsonTrailer: Service[JsonRequest, JsonResponseTrailer]
  def loadJsonImages: Service[JsonRequest, JsonResponseImages]
  def loadJsonMovieDetail: Service[JsonRequest, JsonResponseMovieDetail]
}

trait JsonServicesComponent {
  val jsonServices: JsonServices
}
