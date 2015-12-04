import sbt._

object Libraries {

  def onCompile(dep: ModuleID): ModuleID = dep % "compile"
  def onTest(dep: ModuleID): ModuleID = dep % "test"

  object scala {

    lazy val scalaReflect = "org.scala-lang" % "scala-reflect" % Versions.scalaV
    lazy val scalap = "org.scala-lang" % "scalap" % Versions.scalaV
  }

  object android {

    def androidDep(module: String) = "com.android.support" % module % Versions.androidV

    lazy val androidRecyclerview = androidDep("recyclerview-v7")
    lazy val androidCardView = androidDep("cardview-v7")
    lazy val androidDesign = androidDep("design")
  }
  object qr {
    lazy val zxingCore = "com.google.zxing" % "core" % Versions.zxingCoreV
    lazy val zxingAndroid = "com.embarkmobile" % "zxing-android-minimal" % Versions.zxingAndroidV
  }


  object playServices {

    def playServicesDep(module: String) = "com.google.android.gms" % module % Versions.playServicesV

    lazy val playServicesGooglePlus = playServicesDep("play-services-plus")
    lazy val playServicesAccountLogin = playServicesDep("play-services-identity")
    lazy val playServicesActivityRecognition = playServicesDep("play-services-location")
    lazy val playServicesAppIndexing = playServicesDep("play-services-appindexing")
    lazy val playServicesCast = playServicesDep("play-services-cast")
    lazy val playServicesDrive = playServicesDep("play-services-drive")
    lazy val playServicesFit = playServicesDep("play-services-fitness")
    lazy val playServicesMaps = playServicesDep("play-services-maps")
    lazy val playServicesAds = playServicesDep("play-services-ads")
    lazy val playServicesPanoramaViewer = playServicesDep("play-services-panorama")
    lazy val playServicesGames = playServicesDep("play-services-games")
    lazy val playServicesWallet = playServicesDep("play-services-wallet")
    lazy val playServicesWear = playServicesDep("play-services-wearable")
    // Google Actions, Google Analytics and Google Cloud Messaging
    lazy val playServicesBase = playServicesDep("play-services-base")
  }

  object graphics {
    lazy val picasso = "com.squareup.picasso" % "picasso" % Versions.picassoV
    lazy val circleimageview = "de.hdodenhof" % "circleimageview"  % Versions.circleimageviewV
  }

  object akka {

    def akka(module: String) = "com.typesafe.akka" %% s"akka-$module" % Versions.akkaV

    lazy val akkaActor = akka("actor")
    lazy val akkaTestKit = akka("testkit")

  }

  object macroid {

    def macroid(module: String = "") =
      "org.macroid" %% s"macroid${if(!module.isEmpty) s"-$module" else ""}" % Versions.macroidV

    lazy val macroidRoot = macroid()
    lazy val macroidAkkaFragments = macroid("akka")
    lazy val macroidExtras = "com.fortysevendeg" %% "macroid-extras" % Versions.macroidExtras
  }

  object json {
    lazy val playJson = "com.typesafe.play" %% "play-json" % Versions.playJsonV
  }

  object net {
    lazy val communicator = "io.taig" %% "communicator" % Versions.communicatorV

  }

  object test {
    lazy val specs2 = "org.specs2" %% "specs2-core" % Versions.specs2V % "test"
    lazy val androidTest = "com.google.android" % "android" % "4.1.1.4" % "test"
    lazy val mockito = "org.specs2" % "specs2-mock_2.11" % Versions.mockitoV % "test"
  }
}