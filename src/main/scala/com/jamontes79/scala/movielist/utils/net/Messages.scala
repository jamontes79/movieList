package com.jamontes79.scala.movielist.utils.net

/**
 * Created by alberto on 29/9/15.
 */
case class NetRequest(url : String, forceDownload: Boolean)

case class NetResponse(success: Boolean,
                       downloaded: Boolean)


