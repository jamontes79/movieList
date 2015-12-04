import sbt._

object Settings {

  lazy val resolvers =
    Seq(
      Resolver.mavenLocal,
      DefaultMavenRepository,
      "jcenter" at "http://jcenter.bintray.com",
      "47 Degrees Bintray Repo" at "http://dl.bintray.com/47deg/maven",
      Resolver.typesafeRepo("releases"),
      Resolver.typesafeRepo("snapshots"),
      Resolver.typesafeIvyRepo("snapshots"),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots"),
      Resolver.defaultLocal,
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      "47deg Public" at "http://clinker.47deg.com/nexus/content/groups/public",
      "zxing Android Minimal" at "https://raw.github.com/embarkmobile/zxing-android-minimal/mvn-repo/maven-repository/",
      "Crashlytics" at "http://download.crashlytics.com/maven"
    )
/*
"-keep class twitter4j.** { *; }",
    "-keep class org.joda.** { *; }",
    "-keep class org.ocpsoft.prettytime.i18n.**",
    "-keep class macroid.** { *; }",
    "-keep class com.google.** { *; }",
    "-keep class com.localytics.android.** { *; }",
    "-keep class com.google.android.gms.ads.** { *; }",
    "-keep class com.crashlytics.** { *; }",
    "-keep class * extends java.util.ListResourceBundle { protected Object[][] getContents(); }",
    "-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable { public static final *** NULL; }",
    "-keepnames @com.google.android.gms.common.annotation.KeepName class *",
    "-keepclassmembernames class * { @com.google.android.gms.common.annotation.KeepName *; }",
    "-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }"
	*/
lazy val proguardCommons = Seq(
  "-ignorewarnings",
  "-keep class scala.Dynamic",
  "-keep class com.fortysevendeg.scala.android.** { *; }",
  "-keep class macroid.** { *; }",
  "-keep class com.jamontes79.** { *; }",
  "-keep class android.** { *; }")

}
