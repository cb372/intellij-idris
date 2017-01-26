scalaVersion := "2.12.1"

unmanagedSourceDirectories in Compile += baseDirectory.value / "src"
unmanagedSourceDirectories in Test += baseDirectory.value / "test"
unmanagedResourceDirectories in Compile += baseDirectory.value / "resources"

libraryDependencies ++= Seq(
  "org.parboiled" %% "parboiled" % "2.1.3",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

enablePlugins(SbtIdeaPlugin)
