package com.jamontes79.scala.movielist.entities

import java.io.{File, FileInputStream, FileOutputStream}

import android.util.Log
import com.jamontes79.MyUtil
import com.jamontes79.database.{DBUtil, DataDictionary}
import com.jamontes79.scala.movielist.utils.MyUtils

case class Root(
                 movies: Seq[Movie])

/**
 * Created by alberto on 3/9/15.
 */
case class Movie(
                     var id: Option[String] = None,
                     var title: String = "",
                     var format: String = "",
                     var gender: Option[String] = None,
                     var loan: String = "",
                     var cover: Option[String] = None,
                     var duration: String = "",
                     var sinopsis: Option[String] = None,
                     var rating: Float = 0,
                     var imdb: Option[String] = None,
                     var thumb: Option[String] = None,
                     var fullimdbId: Option[String] = None,
                     var barcode: Option[String] = None) {

  def scape(texto: Option[String]): String = {
    return texto.getOrElse("").replaceAll("\'", "\\''")
  }

  def save: Boolean = {
    var sql = ""
    Log.e("imdb",imdb.getOrElse(""))
    Log.e("fullimdb",fullimdbId.getOrElse(""))
    if (id.getOrElse("").trim.length == 0) {
      id = Option(MyUtil.getNewCounter(MyUtils.COUNTER_PELICULA, DataDictionary.DV_CONTADORES))
      sql = "INSERT INTO " + DataDictionary.DV_PELICULAS + "(PELICULAID, TITULO, TITULOSINACENTO,FORMATO, GENERO, " +
        "PRESTADA, DURACION, SINOPSIS, PUNTUACION,IMDBID,VSYNFLAG,NEW_IMDBID) " +
        " VALUES (" +
        "'" + id.get + "'," +
        "'" + DBUtil.stringEscapeToSQL(title) + "'," +
        "'" + DBUtil.stringEscapeToSQL(title) + "'," +
        "'" + format + "'," +
        "'" + gender.getOrElse("") + "'," +
        "'" + loan + "'," +
        "'" + duration + "'," +
        "'" + DBUtil.stringEscapeToSQL(sinopsis.getOrElse("")) + "'," +
        "'" + rating + "'," +
        "'" + fullimdbId.getOrElse("") + "'," +
        "'I'," +
        "'" + imdb.getOrElse("") + "')"


    }
    else {
      sql = "UPDATE " + DataDictionary.DV_PELICULAS +
        " SET TITULO = '" + DBUtil.stringEscapeToSQL(title) + "'," +
        " TITULOSINACENTO = '" + DBUtil.stringEscapeToSQL(title) + "'," +
        " FORMATO = '" + format + "'," +
        " GENERO = '" + gender.getOrElse("") + "'," +
        " PRESTADA = '" + loan + "'," +
        " DURACION = '" + duration + "'," +
        " SINOPSIS =  '" + DBUtil.stringEscapeToSQL(sinopsis.getOrElse("")) + "'," +
        " PUNTUACION = '" + rating + "'," +
        " IMDBID = '" + fullimdbId.getOrElse("") + "'," +
        " NEW_IMDBID = '" + imdb.getOrElse("") + "'" +
        " WHERE PELICULAID = '" + id.getOrElse("") + "'"

    }
    Log.e("sql", sql)
    DBUtil.m_appConnection.execSQL(sql)

    cover match {
      case None => {

        val dir = new File(MyUtils.STORAGE_IMAGES)
        val output = new File(dir, id + ".png")
        if (output.exists()) {
          output.delete
        }
        val source = new File(dir, MyUtils.TEMP_IMAGE)
        if (source.exists()) {
          source.delete
        }
      }
      case Some(p) => {

        val dir = new File(MyUtils.STORAGE_IMAGES)
        val source = new File(dir, MyUtils.TEMP_IMAGE)
        val output = new File(dir, id.get + ".png")
        if (source.exists()) {
          if (output.exists()) {
            output.delete
          }
          val srcChannel = new FileInputStream(source.getAbsolutePath).getChannel()
          val dstChannel = new FileOutputStream(output.getAbsolutePath).getChannel()
          dstChannel.transferFrom(srcChannel, 0, srcChannel.size())
          source.delete()
        }


        val sourceThmb = new File(dir, MyUtils.TEMP_THMB_IMAGE)
        val outputThmb = new File(dir, id.get + "_th.png")
        if (sourceThmb.exists()) {
          if (outputThmb.exists()) {
            outputThmb.delete
          }
          val srcChannelThmb = new FileInputStream(sourceThmb.getAbsolutePath).getChannel()
          val dstChannelThmb = new FileOutputStream(outputThmb.getAbsolutePath).getChannel()
          dstChannelThmb.transferFrom(srcChannelThmb, 0, srcChannelThmb.size())
          sourceThmb.delete()
        }


      }
    }


    true
  }

  def delete: Unit = {
    val sql = "delete FROM " + DataDictionary.DV_PELICULAS +
      " WHERE PELICULAID = '" + id.get + "'"
    DBUtil.m_appConnection.execSQL(sql)

    if (cover.getOrElse("").length > 0) {


      val dir = new File(MyUtils.STORAGE_IMAGES)
      val source = new File(dir, id.get + ".png")

      if (source.exists()) {
        source.delete
      }
      val sourceThmb = new File(dir, id.get + "_th.png")
      if (sourceThmb.exists()) {
        sourceThmb.delete
      }
    }

  }



  def reloadData: Unit = {
    val sql = "SELECT PELICULAID, TITULO, FORMATO, GENERO, PRESTADA, DURACION, SINOPSIS, PUNTUACION, CARATULA, TITULOSINACENTO," +
      " TRAILER, BUSCADA, THUMBNAIL, IMDBID, VSYNFLAG, NEW_IMDBID FROM " + DataDictionary.DV_PELICULAS +
      " WHERE PELICULAID = '" + id.get + "'"


    val c = DBUtil.m_appConnection.rawQuery(sql, null);
    try {
      while (c.moveToNext()) {
        var cover = Option("")
        var thum = Option("")
        val hasImage = if (new File(MyUtils.SDCARD_VIDEO_DROID + "/" + c.getString(0) + ".png").exists) true else !c.isNull(8)
        if (hasImage) {

          cover = Option(MyUtils.SDCARD_VIDEO_DROID + "/" + c.getString(0) + ".png")
          thum = Option(MyUtils.SDCARD_VIDEO_DROID + "/" + c.getString(0) + "_th.png")
        }
        else {
          cover = None
          thum = None
        }
        id = Option(c.getString(0))
        title =  c.getString(1)
        format = c.getString(2)
        gender =  Option(c.getString(3))
        loan =  c.getString(4)
        duration = c.getString(5)
        sinopsis =Option(c.getString(6))
        rating = c.getFloat(7)
        cover =  cover
        thumb = thum
        imdb = Option(c.getString(15))
        fullimdbId = Option(c.getString(13))

      }
    } finally c.close()
  }

}

object Movie {
  def loadAllMovies: Array[Movie] = {


    val sql = "SELECT PELICULAID, TITULO, FORMATO, GENERO, PRESTADA, DURACION, SINOPSIS, PUNTUACION, CARATULA, TITULOSINACENTO," +
      " TRAILER, BUSCADA, THUMBNAIL, IMDBID, VSYNFLAG, NEW_IMDBID FROM " + DataDictionary.DV_PELICULAS +
      " ORDER BY TITULO"



    var pelis: Array[Movie] = Array()
    val c = DBUtil.m_appConnection.rawQuery(sql, null);
    try {
      while (c.moveToNext()) {
        var caratula = Option("")
        var thum = Option("")
        val hasImage = if (new File(MyUtils.SDCARD_VIDEO_DROID + "/" + c.getString(0) + ".png").exists) true else !c.isNull(8)
        if (hasImage) {

          caratula = Option(MyUtils.SDCARD_VIDEO_DROID + "/" + c.getString(0) + ".png")
          thum = Option(MyUtils.SDCARD_VIDEO_DROID + "/" + c.getString(0) + "_th.png")
        }
        else {
          caratula = None
          thum = None
        }
        pelis = pelis :+ new Movie(Option(c.getString(0)),
          c.getString(1),
          c.getString(2),
          Option(c.getString(3)),
          c.getString(4),
          caratula,
          c.getString(5),
          Option(c.getString(6)),
          c.getFloat(7),
          Option(c.getString(15)),
          thum,
          Option(c.getString(13)))
      }
    } finally c.close()

    pelis

  }
}