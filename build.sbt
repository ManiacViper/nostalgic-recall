val Http4sVersion = "0.23.28"
val LogbackVersion = "1.5.8"
val CatsVersion = "2.12.0"
val CatsEffectVersion = "3.5.4"
val CirceVersion = "0.14.9"

lazy val root = (project in file("."))
  .settings(
    organization := "org",
    name := "nostalgic-recall",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.14",
    idePackagePrefix := Some("com.nostalgia"),
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe"        %% "circe-parser"        % CirceVersion,
      "org.typelevel"   %% "cats-core"           % CatsVersion,
      "org.typelevel"   %% "cats-effect"         % CatsEffectVersion,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.scalatest"   %% "scalatest"           % "3.2.19"           % Test

    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.3" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("org.scalatest.tools.Framework"),
    Test / parallelExecution := false
  )


