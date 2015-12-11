package com.jamontes79.scala.movielist.composers

import com.jamontes79.scala.movielist.{TR, TypedFindView}


trait ViewComposer {
  self: TypedFindView =>

  lazy val cardView = Option(findView(TR.cardView))
  lazy val toolBar = Option(findView(TR.detail_toolbar))
  lazy val lblTitle = Option(findView(TR.lblTitulo))
  lazy val imgCover = Option(findView(TR.imgCover))
  lazy val lblFormat = Option(findView(TR.lblFormato))
  lazy val lblGender = Option(findView(TR.lblGenero))
  lazy val lblLoan = Option(findView(TR.lblPrestado))
  lazy val lblDuration = Option(findView(TR.lblDuracion))
  lazy val rtRating = Option(findView(TR.rtPuntuacion))
  lazy val lblSinopsis = Option(findView(TR.lblSinopsis))
  lazy val fabButton = Option(findView(TR.fab))



}

trait DetailComposer {
  self: TypedFindView =>
  lazy val nestedScrollView = Option(findView(TR.nestedScrollView))
  lazy val progressBar = Option(findView(TR.progressBar))
  lazy val cardView = Option(findView(TR.cardView))
  lazy val fabActionButton = Option(findView(TR.fabSave))
  lazy val toolBar = Option(findView(TR.detail_toolbar))
  lazy val txtTitle = Option(findView(TR.txtTitle))
  lazy val imgCover = Option(findView(TR.imgCover))
  lazy val cmbFormat = Option(findView(TR.cmbFormat))
  lazy val cmbGender = Option(findView(TR.cmbGender))
  lazy val tgLoan = Option(findView(TR.tglLoan))
  lazy val txtDuration = Option(findView(TR.txtDuration))
  lazy val rtRating = Option(findView(TR.rtEvaluation))
  lazy val txtSinopsis = Option(findView(TR.txtSinopsis))
  lazy val bntSearchTitleImdb = Option(findView(TR.btnSearchImdb))



}
