package com.jamontes79.scala.movielist.utils

import android.content.Context
import android.os.Environment
import com.jamontes79.database.{DBUtil, DataDictionary}
import com.jamontes79.scala.movielist.utils.json.entities.ApiConfiguration

import scala.concurrent.Future

object MyUtils{
  var config = new ApiConfiguration
  type Service[Req, Res] = Req => Future[Res]
  val  IMDB_BASE_URL = "http://m.imdb.com/title/"
  val EXTRA_IMAGE  = "com.maraldesarrollos.videodroid.extraImage"
  val EXTRA_TITLE = "com.maraldesarrollos.videodroid.extraTitle"
  val EXTRA_VIDEOURL = "com.maraldesarrollos.videodroid.extraVideo"
  val EXTRA_DESCRIPTION = "com.maraldesarrollos.videodroid.extraDescription"
  val EXTRA_OBJECT= "com.maraldesarrollos.videodroid.extraObject"
  val NEW_REQCODE = 1002
  val MODIFY_REQCODE = 1003
  val IMAGE_REQCODE = 1004
  val SDCARD_VIDEO_DROID = "/sdcard/movieList"
  val DATABASENAME = "maraldesarrollosVIDEO"
  val TAKE_PICTURE = 1
  val SELECT_PICTURE = 2
  val STORAGE_IMAGES = Environment.getExternalStorageDirectory() + "/movieList/"
  val TEMP_IMAGE = "temp.png"
  val TEMP_THMB_IMAGE = "temp_thmb.png"
  val COUNTER_PELICULA = "PEL"
  val INITIAL_COUNTER = "0000"



}
/**
 * Created by alberto on 8/9/15.
 */
trait MyUtils {

  def checkDataBase(pContext : Context ) {
    DBUtil.openDatabase(pContext,MyUtils.SDCARD_VIDEO_DROID,MyUtils.DATABASENAME,DataDictionary.getTables(),DataDictionary.getTablesFields(),DataDictionary.getTableKeys(),DataDictionary.getTableTypes());
  }
}
