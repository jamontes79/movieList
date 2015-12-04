package com.jamontes79.scala.movielist.dialog

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.ViewGroup.LayoutParams._
import android.view._
import android.widget.{ImageView, TextView, LinearLayout}
import com.fortysevendeg.macroid.extras.ImageViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.jamontes79.scala.movielist.{DetailActivity, R}
import macroid.{Ui, Contexts}
import macroid.FullDsl._

/**
 * Created by alberto on 9/9/15.
 */
class SelectImageDialog extends DialogFragment
with Contexts[DialogFragment]
{
  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    val style = DialogFragment.STYLE_NO_TITLE
    val theme = android.R.style.Theme_Holo_Light_Dialog_NoActionBar

    setStyle(style, theme)
  }

  override def onActivityCreated(savedInstanceState: Bundle): Unit = {
    super.onActivityCreated(savedInstanceState)
    getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation

    val window = getDialog().getWindow()
    window.setGravity(Gravity.BOTTOM)
    val lp = getDialog().getWindow().getAttributes()
    lp.dimAmount = 0.7f
    getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    window.setLayout(MATCH_PARENT, WRAP_CONTENT)
  }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    super.onCreateView(inflater, container, savedInstanceState)
   // content
    getUi(
      l[LinearLayout](
        w[TextView] <~ tvText(resGetString(R.string.caratula)),
        l[LinearLayout](
          w[ImageView] <~ ivSrc(android.R.drawable.ic_menu_gallery) <~ On.Click{
            Ui {
               getActivity.asInstanceOf[DetailActivity].selectGallery
               dismiss()
            }
          },
          w[TextView] <~ tvText(resGetString(R.string._gallery))<~ On.Click{
            Ui {
              getActivity.asInstanceOf[DetailActivity].selectGallery
              dismiss()
            }
          }
        )<~ horizontal,
        l[LinearLayout](
          w[ImageView] <~ ivSrc(android.R.drawable.ic_menu_camera)<~ On.Click{
            Ui {
              getActivity.asInstanceOf[DetailActivity].selectCamera
              dismiss()
            }
          },
          w[TextView] <~ tvText(resGetString(R.string._camera))<~ On.Click{
            Ui {
              getActivity.asInstanceOf[DetailActivity].selectCamera
              dismiss()
            }
          }
        )<~ horizontal,
        l[LinearLayout](
          w[ImageView] <~ ivSrc(android.R.drawable.ic_menu_close_clear_cancel)<~ On.Click{
            Ui {
              getActivity.asInstanceOf[DetailActivity].deleteImage
              dismiss()
            }
          },
          w[TextView] <~ tvText(resGetString(R.string._delete))<~ On.Click{
            Ui {
              getActivity.asInstanceOf[DetailActivity].deleteImage
              dismiss()
            }
          }
        )<~ horizontal

      )<~ vertical <~ padding(all=40)

    )
  }
}
