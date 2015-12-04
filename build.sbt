import Libraries.android._
import Libraries.graphics._
import Libraries.json._
import Libraries.macroid._
import Libraries.qr._
import ReplacePropertiesGenerator._
import android.PromptPasswordsSigningConfig


android.Plugin.androidBuild

platformTarget in Android := Versions.androidPlatformV

name := """MovieList"""

organization := "com.jamontes79"

organizationName := "Alberto Montes de Oca"

organizationHomepage := None

version := Versions.appV

scalaVersion := Versions.scalaV

unmanagedBase := baseDirectory.value / "src" / "main" / "libs"

scalacOptions ++= Seq("-feature", "-deprecation")

resolvers ++= Settings.resolvers
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
scalacOptions ++= Seq("-feature", "-deprecation", "-target:jvm-1.7")
libraryDependencies ++= Seq(
  aar(macroidRoot),
  aar(macroidAkkaFragments),
  aar(androidDesign),
  aar(androidCardView),
  aar(androidRecyclerview),
  aar(macroidExtras),
  playJson,
  picasso,
  zxingCore,
  aar(zxingAndroid))


proguardCache in Android := Seq.empty

dexMaxHeap in Android := "2048m"

run <<= (run in Android).dependsOn(setDebugTask(true))

apkSigningConfig in Android := Option(
  PromptPasswordsSigningConfig(
    keystore = new File(Path.userHome.absolutePath + "/.android/signed.keystore"),
    alias = "jamontes79"))

packageRelease <<= (packageRelease in Android).dependsOn(setDebugTask(false))

proguardScala in Android := true

useProguard in Android := true

proguardOptions in Android ++= Settings.proguardCommons

apkbuildExcludes in Android ++= Seq(
  "META-INF/LICENSE",
  "META-INF/LICENSE.txt",
  "META-INF/NOTICE",
  "META-INF/NOTICE.txt")

packageResources in Android <<= (packageResources in Android).dependsOn(replaceValuesTask)
