lazy val root = (project in file(".")).
settings(
  name := "crypto",
  organization <<= name,
  version := "1.0",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-deprecation", "-optimise", "-feature", "-Yinline-warnings"),

  libraryDependencies ++= Seq(
	"org.bouncycastle" % "bcprov-jdk15on" % "1.54"
 ),

  publishMavenStyle := true,
  publishTo := Some(Resolver.file("Local", Path.userHome / ".m2" / "repository" asFile)),

  initialCommands in console := """
    |import crypto.Project1._
    |import crypto.CypherText._
    |""".stripMargin
)
