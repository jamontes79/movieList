package com.jamontes79.scala.movielist

/*
import com.fortysevendeg.android.scaladays.model.Speaker
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.ui.commons.AnalyticStrings._
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, UiServices, LineItemDecorator}
*/

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view._
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.layouts.ImageFragmentLayout
import com.jamontes79.scala.movielist.utils.AsyncImageTweaks._
import com.jamontes79.scala.movielist.utils.MyUtils
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts}


/**
 * Created by alberto on 2/9/15.
 */
class ImageFragment extends Fragment
with Contexts[Fragment]
with ImageFragmentLayout
with MyUtils
{

  lazy val contextProvider: ContextWrapper = fragmentContextWrapper
  var currentMovie: Movie = new Movie

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
setHasOptionsMenu(true)
    val (maybeimage, maybepelicula) = Option(getActivity.getIntent.getExtras) map {
      extras =>

        val image: Option[String] = if (extras.containsKey(MyUtils.EXTRA_IMAGE))
          Some(extras.getString(MyUtils.EXTRA_IMAGE))
        else None
        val movie: Option[Movie] = if (extras.containsKey(MyUtils.EXTRA_OBJECT))
          Some(extras.getSerializable(MyUtils.EXTRA_OBJECT).asInstanceOf[Movie])

        else None
        (image, movie)
    } getOrElse ((None, None))
    currentMovie = maybepelicula.get

    content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)

    getActivity.setTitle(currentMovie.title)


    runUi(

        imageOnView <~
          (currentMovie.cover map {
            srcImageFile(_, R.drawable.placeholder_circle, Some(R.drawable.no_disponible))
          } getOrElse ivSrc(R.drawable.no_disponible))


    )


  }




  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)

  }






  override def onOptionsItemSelected(item: MenuItem): Boolean = {

    if (item.getItemId == android.R.id.home){

      getActivity.setResult(Activity.RESULT_CANCELED)
      getActivity.finish()
      true
    }
    else {
      super.onOptionsItemSelected(item)
    }
  }
}