package com.jamontes79.scala.movielist

import java.io.{BufferedInputStream, File, FileInputStream, FileOutputStream}
import java.net.URL

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.{Bitmap, BitmapFactory, Matrix}
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.{ActivityCompat, ActivityOptionsCompat, FragmentActivity}
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View.OnTouchListener
import android.view.{Menu, MenuItem, MotionEvent, View}
import android.widget.ArrayAdapter
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.google.zxing.integration.android.IntentIntegrator
import com.jamontes79.MyUtil
import com.jamontes79.scala.movielist.composers.DetailComposer
import com.jamontes79.scala.movielist.dialog.SelectImageDialog
import com.jamontes79.scala.movielist.entities.Movie
import com.jamontes79.scala.movielist.utils.AsyncImageTweaks._
import com.jamontes79.scala.movielist.utils.json.entities.ApiConfiguration
import com.jamontes79.scala.movielist.utils.{ComponentRegistryImpl, ImdbServices, MyUtils}
import macroid.FullDsl._
import macroid.{ContextWrapper, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future
import scala.sys.process._
import scala.util.{Failure, Success}
/**
 * Created by alberto on 6/11/15.
 */
class DetailActivity extends AppCompatActivity
with TypedFindView
with DetailComposer
with Contexts[AppCompatActivity]
with ImdbServices
with ComponentRegistryImpl {

  var currentMovie: Movie = new Movie


  lazy val contextProvider: ContextWrapper = activityContextWrapper

  val MENU_SEARCH_IMDB = 0
  lazy val adapterFormat: ArrayAdapter[CharSequence] = ArrayAdapter.createFromResource(this, R.array._formats, android.R.layout.simple_spinner_item)
  lazy val adapterGender: ArrayAdapter[CharSequence] = ArrayAdapter.createFromResource(this, R.array._genders, android.R.layout.simple_spinner_item)
  var hasImage = false

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_detail_coordinator)

    toolBar foreach setSupportActionBar
    getSupportActionBar.setDisplayHomeAsUpEnabled(true)

    val (maybeimage, maybepelicula) = Option(getIntent.getExtras) map {
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
    adapterFormat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    loadData
  }





  def loadData : Unit = {
    setTitle(currentMovie.title)
    toolBar.get.setTitle(currentMovie.title)

    cmbFormat.get.setAdapter(adapterFormat)
    cmbGender.get.setAdapter(adapterGender)
    tgLoan.get.setChecked(currentMovie.loan == "1")
    cmbFormat.get.setSelection(adapterFormat.getPosition(currentMovie.format), true)
    currentMovie.gender match {
      case Some(x) => cmbGender.get.setSelection(adapterGender.getPosition(x), true)
      case _ => cmbGender.get.setSelection(0, true)
    }

    runUi(
      (fabActionButton

        <~ On.click {
        Ui {
          save
        }
      }) ~

      (imgCover <~
        (currentMovie.cover map {
          srcImageFile(_, R.drawable.placeholder_circle, Some(R.drawable.no_disponible))
        } getOrElse ivSrc(R.drawable.no_disponible))) ~
        (txtTitle <~ tvText(currentMovie.title)) ~

        (bntSearchTitleImdb <~ On.click(
          Ui {
            searchImdb
          }
        )) ~
        (txtSinopsis <~ tvText((currentMovie.sinopsis.getOrElse("")))) ~
        (txtDuration <~ (currentMovie.duration map {
          tvText(_)
        }))


    )


    rtRating.get.setRating(currentMovie.rating)
    rtRating.get.setEnabled(true)
    tgLoan.get.setChecked(currentMovie.loan == "1")


    imgCover.get.setOnTouchListener(new OnTouchListener {
      override def onTouch(view: View, motionEvent: MotionEvent): Boolean = {
        if (motionEvent.getAction == MotionEvent.ACTION_DOWN){
            showImageDialog
        }
        return true
      }
    })
    ViewCompat.setTransitionName(imgCover.get, MyUtils.EXTRA_IMAGE)


  }

  

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.menu_detail, menu)
    return true
  }
  def searchImdb: Unit = {
    val detail = new Intent(this.asInstanceOf[DetailActivity], classOf[SearchActivity])
    detail.putExtra("titleToSearch", txtTitle.get.getText.toString)
    startActivityForResult(detail, MENU_SEARCH_IMDB)
  }

  def showImageDialog: Unit = {
    val fm = this.getSupportFragmentManager
    val selectImageDialog = new SelectImageDialog
     // Show DialogFragment
    selectImageDialog.show(fm, "Dialog Fragment");
  }

  def setBarCode(barcode : String) = {
    currentMovie.barcode = Some(barcode)
    searchTitle(barcode).onComplete{
      case Success(titleFound) => {
        runUi(
          (txtTitle <~ tvText(titleFound))

        )
        runUi {(macroid.DialogBuilding.dialog(getString(R.string._barcode_found)) <~
          positiveYes( Ui {
             searchImdb
          }) <~ title( getString(R.string._barcode)) <~
          negativeNo(Ui {}))<~ speak}


      }
      case Failure(t) => runUi {toast(R.string._barcode_not_found)<~ fry}
    }


  }
  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
    if (scanResult != null && resultCode == Activity.RESULT_OK) {
      // handle scan result
      val currEan = scanResult.getContents()
      setBarCode(currEan)
    }

    else {

      if (requestCode == MyUtils.SELECT_PICTURE) {
        if (data != null) {
          val fileName = MyUtil.getRealPathFromURI(this, data.getData)

          val dir = new File(MyUtils.STORAGE_IMAGES)
          if (!dir.exists()) {
            dir.mkdirs()
          }
          val output = new File(dir, MyUtils.TEMP_IMAGE)
          val srcChannel = new FileInputStream(fileName).getChannel()
          val dstChannel = new FileOutputStream(output.getAbsolutePath).getChannel()
          dstChannel.transferFrom(srcChannel, 0, srcChannel.size())
          hasImage = true
          runUi(
            imgCover <~
              ivSrc(BitmapFactory.decodeStream(new BufferedInputStream(this.getContentResolver.openInputStream(data.getData()))))

          )
        }
      }
      else if (requestCode == MyUtils.TAKE_PICTURE) {
        val o = new BitmapFactory.Options()
        o.inSampleSize = 8

        val dir = new File(MyUtils.STORAGE_IMAGES)
        val output = new File(dir, MyUtils.TEMP_IMAGE)
        val imageName = output.getAbsolutePath()
        var tmpBmp = BitmapFactory.decodeFile(imageName, o)

        if (tmpBmp.getWidth() > tmpBmp.getHeight()) {
          val matrix = new Matrix()
          matrix.postRotate(90)
          tmpBmp = Bitmap.createBitmap(tmpBmp, 0, 0, tmpBmp.getWidth(), tmpBmp.getHeight(), matrix, true)
        }
        tmpBmp = Bitmap.createScaledBitmap(tmpBmp, imgCover.get.getWidth(), imgCover.get.getHeight(), true)
        hasImage = true
        runUi(
          imgCover <~
            ivSrc(tmpBmp)
        )

      }
      else if (requestCode == MENU_SEARCH_IMDB && resultCode == Activity.RESULT_OK) {
        val extras = data.getExtras()
        if (currentMovie.id.getOrElse("").trim.length == 0) {
          currentMovie = new Movie()
        }
        currentMovie.imdb = Some(extras.getString("imdbId"))
        currentMovie.title = extras.getString("titulo")
        currentMovie.cover = Some(extras.getString("caratula"))
        currentMovie.sinopsis = Some(extras.getString("sinopsis"))
        currentMovie.thumb = Some(extras.getString("thumb"))
        currentMovie.duration = extras.getString("duracion")
        currentMovie.rating = extras.getFloat("puntuacion")
        currentMovie.fullimdbId = Some(extras.getString("fullimdbId"))
        if (currentMovie.cover.getOrElse("").length > 0) {
          hasImage = true
        }
        else {
          hasImage = false
        }
        rtRating.get.setRating(currentMovie.rating)
        runUi(
          (imgCover <~
            (currentMovie.cover map {
              srcImage(_, R.drawable.placeholder_circle, Some(R.drawable.no_disponible))
              //roundedImage(_, R.drawable.placeholder_circle, avatarSize, Some(R.drawable.placeholder_avatar_failed))
            } getOrElse ivSrc(R.drawable.no_disponible))) ~
            (txtSinopsis <~ tvText((currentMovie.sinopsis.getOrElse("")))) ~
            (txtTitle <~ tvText(currentMovie.title)) ~
            (txtDuration <~ tvText(currentMovie.duration))


        )
      }
    }
  }


  def deleteImage: Unit = {
    hasImage = false
    currentMovie.cover = None
    runUi(
      imgCover <~
        ivSrc(R.drawable.no_disponible)
    )
  }

  def cancelSearch: Unit = {

  }
  def selectBarCode =  {
    IntentIntegrator.initiateScan(this)
  }

  def save: Unit = {
    progressBar.get.setVisibility(View.VISIBLE)
    currentMovie.title = txtTitle.get.getText.toString
    currentMovie.format = cmbFormat.get.getSelectedItem.toString
    currentMovie.gender = Option(cmbGender.get.getSelectedItem.toString)
    currentMovie.loan = if (tgLoan.get.getText.equals(tgLoan.get.getTextOn())) "1" else "0"

    val caratulaDesc = downloadFile(currentMovie.cover, MyUtils.STORAGE_IMAGES + MyUtils.TEMP_IMAGE)
    val thumbDesc = downloadFile(currentMovie.thumb, MyUtils.STORAGE_IMAGES + MyUtils.TEMP_THMB_IMAGE)
    if (hasImage) {
      if (!currentMovie.cover.getOrElse("").startsWith("http://")) {


        val thumb = ThumbnailUtils.extractThumbnail(imgCover.get.getDrawable.asInstanceOf[BitmapDrawable].getBitmap, 50, 50)


        val f = new File(MyUtils.STORAGE_IMAGES + MyUtils.TEMP_THMB_IMAGE)
        val outStream = new FileOutputStream(f)
        thumb.compress(Bitmap.CompressFormat.PNG, 100, outStream)
        currentMovie.cover = Option(MyUtils.STORAGE_IMAGES + MyUtils.TEMP_IMAGE)
        currentMovie.thumb = Option(MyUtils.STORAGE_IMAGES + MyUtils.TEMP_THMB_IMAGE)
      }

    }
    else {
      currentMovie.cover = None
      currentMovie.thumb = None
    }
    currentMovie.duration = txtDuration.get.getText.toString
    currentMovie.sinopsis = Option(txtSinopsis.get.getText.toString)
    currentMovie.rating = rtRating.get.getRating
    if (!hasImage) {
      currentMovie.save
      this.setResult(Activity.RESULT_OK)
      this.finish()


    }

    else {
      caratulaDesc.onComplete {
        case Success(c) =>

          thumbDesc.onComplete {
            case Success(d) =>

              currentMovie.cover = Option(MyUtils.STORAGE_IMAGES + MyUtils.TEMP_IMAGE)
              currentMovie.thumb = Option(MyUtils.STORAGE_IMAGES + MyUtils.TEMP_THMB_IMAGE)
              currentMovie.save
              this.setResult(Activity.RESULT_OK)
              this.finish()


            case Failure(x) => Log.e("Error Thumb", "Error Thumb")
          }
        case Failure(x) => Log.e("Error Caratula", "Error caratula")
      }
    }

  }

  def downloadFile(url: Option[String], dest: String): Future[Boolean] = Future {
    try {
      if (url.getOrElse("").startsWith("http://")) {
        new URL(url.get) #> new File(dest) !!
      }

      true
    } catch {
      case e: java.io.IOException => "error occured"
        false
    }
  }
  def reloadCover : Unit = {
    loadConfiguration(false).onComplete{
      case Success(c) => MyUtils.config = c
      case Failure(t) => MyUtils.config = new ApiConfiguration

    }
    reloadCovers(currentMovie.imdb.getOrElse("")).onComplete{
      case Success(url) => {
        val thumbnail = Option(MyUtils.config.base_url + MyUtils.config.poster_sizes.lift(MyUtils.config.poster_sizes.length-2).getOrElse("")+ url)
        val caratulaUrl = Option(MyUtils.config.base_url +  MyUtils.config.poster_sizes(MyUtils.config.poster_sizes.length-1) + url)
        currentMovie.cover = caratulaUrl
        currentMovie.thumb = thumbnail
        if (currentMovie.cover.getOrElse("").length > 0) {
          hasImage = true;
        }
        else {
          hasImage = false;
        }
        runUi(
          (imgCover <~
            (currentMovie.cover map {
              srcImage(_, R.drawable.placeholder_circle, Some(R.drawable.no_disponible))
              //roundedImage(_, R.drawable.placeholder_circle, avatarSize, Some(R.drawable.placeholder_avatar_failed))
            } getOrElse ivSrc(R.drawable.no_disponible)))


        )
      }
      case Failure(t) => runUi {toast(R.string.no_cover_found)<~ fry}
    }
  }
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case android.R.id.home =>
        this.setResult(Activity.RESULT_CANCELED)
        this.finish()
        true
      case R.id.action_barcode => selectBarCode
      case R.id.action_updateCover => reloadCover
      case _ => super.onOptionsItemSelected(item)

    }
    super.onOptionsItemSelected(item)

  }

  def selectGallery = {
    val intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
    startActivityForResult(intent, MyUtils.SELECT_PICTURE)

  }
  def selectCamera= {
    val intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val dir=new File (MyUtils.STORAGE_IMAGES)
    if (!dir.exists()){
      dir.mkdirs()
    }
    val output=new File(dir, MyUtils.TEMP_IMAGE)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output))
    startActivityForResult(intent, MyUtils.TAKE_PICTURE)
  }

}

object DetailActivity
{
  def navigate(activity: FragmentActivity , transitionImage: View , movie: Movie, requestCode : Int ) {
    val intent = new Intent(activity, classOf[DetailActivity])
    intent.putExtra(MyUtils.EXTRA_IMAGE, movie.cover)
    intent.putExtra(MyUtils.EXTRA_OBJECT,movie)
   
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionImage, MyUtils.EXTRA_IMAGE)

    ActivityCompat.startActivityForResult(activity, intent, requestCode, options.toBundle())
  }
}
