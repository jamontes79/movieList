package com.jamontes79.scala.movielist.utils.json.entities

import com.jamontes79.scala.movielist.entities.{Movie, Root}
import com.jamontes79.scala.movielist.utils.MyUtils


/**
 * Created by alberto on 2/10/15.
 */
trait ApiConversions {



  def toRoot(apiRoot: ApiRoot): Root = {

    Root(apiRoot.results map toPelicula)
  }

  def toPelicula(apiPel: ApiMovie): Movie = {
    //ImdbConfiguration.baseUrlConfiguration + ImdbConfiguration.coverSizes.get(0) +

    var thumbnail = ""

    if (MyUtils.config.poster_sizes.length > 2){

      thumbnail = MyUtils.config.poster_sizes.lift(MyUtils.config.poster_sizes.length-2).getOrElse("")

    }
    else{

      thumbnail = MyUtils.config.poster_sizes.lift(MyUtils.config.poster_sizes.length-1).getOrElse("")

    }

    Movie(title = apiPel.title,
      sinopsis = apiPel.overview,
      cover = Option(MyUtils.config.base_url +  MyUtils.config.poster_sizes(MyUtils.config.poster_sizes.length-1) + apiPel.poster_path.getOrElse("")),
      imdb = Some(apiPel.id.toString),
      thumb = Option(MyUtils.config.base_url +  thumbnail + apiPel.poster_path.getOrElse(""))

    )
  }
}
