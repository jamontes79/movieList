package com.jamontes79.scala.movielist.utils.net


import com.jamontes79.scala.movielist.utils.MyUtils.Service
/**
 * Created by alberto on 29/9/15.
 */
trait NetServices {
  def saveJsonInLocal: Service[NetRequest, NetResponse]
  def loadConfiguration: Service[NetRequest, NetResponse]
  def saveJsonGenericInLocal: Service[NetRequest, NetResponse]

}

trait NetServicesComponent {
  val netServices: NetServices
}
