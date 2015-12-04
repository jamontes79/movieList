package com.jamontes79.scala.movielist

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.{ActivityCompat, ActivityOptionsCompat, FragmentActivity}
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.fortysevendeg.macroid.extras.FragmentExtras._
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.layouts.ImageLayout
import com.jamontes79.scala.movielist.utils.MyUtils
import macroid.FullDsl._
import macroid.{Contexts, IdGeneration}
object ImageActivity
{
  def navigate(activity: FragmentActivity , transitionImage: View , movie: Movie ) {
    val intent = new Intent(activity, classOf[ImageActivity])
    intent.putExtra(MyUtils.EXTRA_IMAGE, movie.cover)
    intent.putExtra(MyUtils.EXTRA_OBJECT,movie)
   
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, MyUtils.EXTRA_IMAGE)

    ActivityCompat.startActivityForResult(activity, intent, MyUtils.IMAGE_REQCODE, options.toBundle())
  }
}
/**
 * Created by alberto on 30/10/15.
 */
class ImageActivity  extends AppCompatActivity
with Contexts[FragmentActivity]
with ImageLayout
with IdGeneration
with MyUtils {

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(layout)


    toolBar map setSupportActionBar

    getSupportActionBar.setDisplayHomeAsUpEnabled(true)
    getSupportActionBar.setHomeButtonEnabled(true)

    if (savedInstanceState == null) {
      runUi(
        replaceFragment(
          builder = f[ImageFragment],
          id = Id.imageFragment,
          tag = Some(Tag.imageFragment)))
    }
  }




}
