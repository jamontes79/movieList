package com.jamontes79.scala.movielist

import java.io.File

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.{ActivityCompat, ActivityOptionsCompat, FragmentActivity}
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View.OnTouchListener
import android.view.{Menu, MenuItem, MotionEvent, View}
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.jamontes79.scala.movielist.composers.ViewComposer
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.utils.AsyncImageTweaks._
import com.jamontes79.scala.movielist.utils.json.entities.ApiConfiguration
import com.jamontes79.scala.movielist.utils.{ComponentRegistryImpl, ImdbServices, MyUtils}
import com.squareup.picasso.Picasso
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{Failure, Success}

/**
 * Created by alberto on 23/11/15.
 */
class ViewActivity extends AppCompatActivity
with TypedFindView
with ViewComposer
with Contexts[AppCompatActivity]
with ImdbServices
with ComponentRegistryImpl {

  var currentMovie: Movie = new Movie
  var hasChanged = false
  lazy val contextProvider: ContextWrapper = activityContextWrapper

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_view)

    toolBar foreach setSupportActionBar
    getSupportActionBar.setDisplayHomeAsUpEnabled(true)

    val (maybeimage, maybeMovie) = Option(getIntent.getExtras) map {
      extras =>

        val image: Option[String] = if (extras.containsKey(MyUtils.EXTRA_IMAGE))
          Some(extras.getString(MyUtils.EXTRA_IMAGE))
        else None
        val movie: Option[Movie] = if (extras.containsKey(MyUtils.EXTRA_OBJECT))
          Some(extras.getSerializable(MyUtils.EXTRA_OBJECT).asInstanceOf[Movie])

        else None
        (image, movie)
    } getOrElse ((None, None))
    currentMovie = maybeMovie.get
    loadData
  }





  def loadData : Unit = {
    setTitle(currentMovie.title)
    rtRating.get.setRating(currentMovie.rating)
    rtRating.get.setEnabled(false)
    val intro= resGetString(R.string._loan)
    var value =""
    currentMovie.loan match {
      case "0" => value = resGetString(R.string._no)
      case "1" => value =resGetString(R.string._yes)
    }
    imgCover.get.setOnTouchListener(new OnTouchListener {
      override def onTouch(view: View, motionEvent: MotionEvent): Boolean = {

        if (motionEvent.getAction == MotionEvent.ACTION_DOWN){

          showImage

        }


        return true
      }
    })
    runUi(
      (fabButton

        <~ On.click {
        Ui {
          viewTrailer
        }
      }) ~
        (lblFormat <~ tvText(currentMovie.format)) ~
        (lblGender <~ tvText(currentMovie.gender.getOrElse(""))) ~
        (


          lblLoan <~ tvText(value)
          )
        ~
        (imgCover <~
          (currentMovie.cover map {
            srcImageFile(_, R.drawable.placeholder_square, Some(R.drawable.no_disponible))
          } getOrElse ivSrc(R.drawable.no_disponible))) ~

        (lblTitle <~ tvText(currentMovie.title) ) ~

        (lblSinopsis <~ tvText((currentMovie.sinopsis.getOrElse("")))) ~
        (lblDuration <~ tvText((currentMovie.duration)))


    )
    lblTitle.get.setVisibility(View.GONE)
    ViewCompat.setTransitionName(imgCover.get, MyUtils.EXTRA_IMAGE)


  }

  def showImage: Unit = {
    ImageActivity.navigate(this,imgCover.get,currentMovie)
  }


  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.menu_view, menu)
    return true
  }
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case android.R.id.home =>

        if (hasChanged){

          setResult(Activity.RESULT_OK)
        }
        else {
          setResult(Activity.RESULT_CANCELED)
        }
        finish()

      case R.id.action_view_imdb => visitImdbMoviePage
      case R.id.action_delete => delete
      case R.id.action_view_trailer => viewTrailer
      case R.id.action_share => share
      case R.id.action_modify => modify
      case _ => super.onOptionsItemSelected(item)
    }

    super.onOptionsItemSelected(item)
  }

  def modify : Unit = {
    DetailActivity.navigate(this,imgCover.get,currentMovie,MyUtils.MODIFY_REQCODE)
  }
  def visitImdbMoviePage : Unit = {

    if (currentMovie.imdb.getOrElse("").length == 0) {
      runUi {toast(R.string.no_imdb_id_found)<~ fry}
    }
    else {
      val imdbUrl = MyUtils.IMDB_BASE_URL + currentMovie.fullimdbId.get
      val imdbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imdbUrl))
      startActivity(imdbIntent)
    }
  }
  def delete : Unit = {
    currentMovie.delete
    setResult(Activity.RESULT_OK)

    finish()

  }
  def viewTrailer : Unit = {
    loadConfiguration(false).onComplete{
      case Success(c) => MyUtils.config = c
      case Failure(t) => MyUtils.config = new ApiConfiguration

    }
    searchTrailer(currentMovie.imdb.getOrElse(""),true).onComplete{
      case Success(url) => {
        if (url.length > 0) {

          val imdbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
          this.startActivity(imdbIntent)
        }
        else{

          runUi {toast(R.string.no_trailer_found)<~ fry}
        }
      }
      case Failure(t) => runUi {toast(R.string.no_trailer_found)<~ fry}
    }
  }



  def share : Unit = {
    val shareIntent = new Intent()
    shareIntent.setAction(Intent.ACTION_SEND)
    val phototUri = Uri.parse("file://" + currentMovie.cover.getOrElse(""))
    shareIntent.putExtra(Intent.EXTRA_TEXT, currentMovie.title)
    shareIntent.putExtra(Intent.EXTRA_STREAM, phototUri)
    shareIntent.setType("image/png")
    startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)))
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    requestCode match {
      case MyUtils.MODIFY_REQCODE => {
        resultCode match {
          case Activity.RESULT_OK => {
            hasChanged = true
            currentMovie.reloadData
            currentMovie.cover match {

              case Some(x) => Picasso.`with`(this).invalidate(new File(x))
              case _ =>

            }
            currentMovie.thumb match {
              case Some(y) => Picasso.`with`(this).invalidate(new File(y))
              case _ =>
            }
            loadData
          }
          case _ =>
        }
      }
      case _ =>

    }
    super.onActivityResult(requestCode, resultCode, data)
  }
  override def onBackPressed(): Unit = {
    if (hasChanged) {
      setResult(Activity.RESULT_OK)
      finish()
    } else {
      super.onBackPressed()
    }
  }
}

object ViewActivity
{
  def navigate(activity: FragmentActivity , transitionImage: View , movie: Movie,requestCode : Int ) {
    val intent = new Intent(activity, classOf[ViewActivity])
    intent.putExtra(MyUtils.EXTRA_IMAGE, movie.cover)
    intent.putExtra(MyUtils.EXTRA_OBJECT,movie)
     
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, MyUtils.EXTRA_IMAGE)

    ActivityCompat.startActivityForResult(activity, intent, requestCode, options.toBundle())
  }
}
