package com.jamontes79.scala.movielist.utils.json.entities

/**
 * Created by alberto on 13/11/15.
 */
case class ApiRootImages(
                           posters: Seq[ApiImages])

case class ApiImages(
                       file_path: String
                       )

