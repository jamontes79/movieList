package com.jamontes79.scala.movielist.utils.json.entities


/**
 * Created by alberto on 7/10/15.
 */
case class ApiConfigRoot(
                    images: ApiConfiguration)

case class ApiConfiguration(
                        base_url: String = "",
                        secure_base_url: String = "",
                       backdrop_sizes: Seq[String] = Seq(),
                        logo_sizes: Seq[String] =  Seq(),
                        poster_sizes: Seq[String] =  Seq(),
                        profile_sizes: Seq[String] =  Seq(),
                        still_sizes: Seq[String] =  Seq())

