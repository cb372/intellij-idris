scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.parboiled" %% "parboiled" % "2.1.3",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

enablePlugins(SbtIdeaPlugin)
