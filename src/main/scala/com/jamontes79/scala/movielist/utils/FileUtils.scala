package com.jamontes79.scala.movielist.utils

import java.io._

import macroid.ContextWrapper

import scala.util.Try
import com.jamontes79.scala.movielist.utils.ResourceUtils._
/**
 * Created by alberto on 29/9/15.
 */
trait FileUtils {

  def loadJsonFile(appContext: ContextWrapper,option : Int): File = {
    option match {
      case 0 =>  new File(MyUtils.STORAGE_IMAGES, StringRes.jsonFilename)
      case 1 => new File(MyUtils.STORAGE_IMAGES, StringRes.jsonConfigFilename)
      case 2 => new File(MyUtils.STORAGE_IMAGES, StringRes.trailerFilename)
    }

  }

  def getJson(file: File): Try[String] =
    Try {
      withResource[FileInputStream, String](new FileInputStream(file)) {
      file => scala.io.Source.fromInputStream(file).mkString
      }
    }

  def writeText(file: File, text: String): Try[Unit] =
    Try {
      withResource[FileOutputStream, Unit](new FileOutputStream(file)) {
        outputStream =>
          val outputWriter = new OutputStreamWriter(outputStream)
          val bufferedWriter = new BufferedWriter(outputWriter, 16384)
          bufferedWriter.write(text)
          bufferedWriter.newLine()
          bufferedWriter.close()
          outputWriter.close()
      }
    }

}
