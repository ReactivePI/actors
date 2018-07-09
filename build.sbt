name := "actors"

version := "0.3.1"

organization := "nl.fizzylogic.reactivepi"

bintrayOrganization := Some("reactivepi")

bintrayReleaseOnPublish in ThisBuild := false

licenses += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

bintrayPackageLabels := Seq("Scala", "IoT", "Raspberry PI")

scalaVersion := "2.12.6"

libraryDependencies ++= {
  val akkaVersion = "2.5.13"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "nl.fizzylogic.reactivepi" %% "core" % "0.3.1"
  )
}
