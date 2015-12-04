package com.jamontes79.scala.movielist.utils.json.entities

/**
 * Created by alberto on 2/10/15.
 */
case class ApiRoot(
                 results: Seq[ApiMovie])

case class ApiMovie(
                          title: String,
                          overview: Option[String],
                          poster_path: Option[String],
                          id : Int
                        )