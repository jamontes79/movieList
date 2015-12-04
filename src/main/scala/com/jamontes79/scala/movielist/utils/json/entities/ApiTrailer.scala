package com.jamontes79.scala.movielist.utils.json.entities

/**
 * Created by alberto on 30/10/15.
 */
case class ApiRootTrailer(
                    youtube: Seq[ApiTrailer])

case class ApiTrailer(
                        name: String,
                        size: String,
                        source: String
                        )
