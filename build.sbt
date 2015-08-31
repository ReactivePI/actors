name := "actors"

version := "0.1"

organization := "reactivepi"

bintrayOrganization := Some("reactivepi")

bintrayReleaseOnPublish in ThisBuild := false

licenses += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

bintrayPackageLabels := Seq("Scala", "IoT", "Raspberry PI")

scalaVersion:= "2.11.6"

libraryDependencies ++= {
  val akkaVersion = "2.3.10"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "reactivepi" %% "core" % "0.1"
  )
}
