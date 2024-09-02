lazy val root = (project in file("."))
  .settings(
    name := "image-magic-api",
    resolvers += Resolver.url(
      "typesafe",
      url("https://repo.typesafe.com/typesafe/ivy-releases/"),
    )(Resolver.ivyStylePatterns),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-streams" % zioVersion,
      "dev.zio" %% "zio-http" % zioHttpVersion,
      "dev.zio" %% "zio-config" % zioConfigVersion,
      "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
      "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
      "ch.qos.logback" % "logback-classic" % logbackClassicVersion,
      "dev.zio" %% "zio-json" % zioJsonVersion,
      "dev.zio" %% "zio-test" % zioVersion,
      "dev.zio" %% "zio-test-sbt" % zioVersion,
      "dev.zio" %% "zio-test-junit" % zioVersion,
      "dev.zio" %% "zio-mock" % zioMockVersion,
      "dev.zio" %% "zio-test-magnolia" % zioVersion,
      "dev.zio" %% "zio-prelude" % zioPreludeVersion,
      "dev.zio" %% "zio-logging" % zioLoggingVersion,
      "dev.zio" %% "zio-logging-slf4j2" % zioLoggingVersion,
      "dev.zio" %% "zio-aws-core" % zioAwsVersion,
      "dev.zio" %% "zio-aws-s3" % zioAwsVersion,
      "dev.zio" %% "zio-aws-netty" % zioAwsVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-armeria-server-zio" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
      "com.lihaoyi" %% "scalatags" % scalatagsVersion,
      "com.twelvemonkeys.imageio" % "imageio-core" % imageIoExtensionVersion,
      "com.twelvemonkeys.imageio" % "imageio-jpeg" % imageIoExtensionVersion,
      "com.twelvemonkeys.imageio" % "imageio-webp" % imageIoExtensionVersion,
      "org.sejda.imageio" % "webp-imageio" % "0.1.6"
    ),
    testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
  )
val usedScalaVersion = "3.4.1"
ThisBuild / scalaVersion := usedScalaVersion

val zioVersion = "2.1.5"
val zioJsonVersion = "0.6.2"
val zioConfigVersion = "4.0.2"
val zioPreludeVersion = "1.0.0-RC25"
val zioLoggingVersion = "2.3.0"
val logbackClassicVersion = "1.5.6"
val zioMockVersion = "1.0.0-RC11"
val zioHttpVersion = "3.0.0-RC1"
val zioAwsVersion = "7.21.15.13"
val tapirVersion = "1.10.12"
val scalatagsVersion = "0.12.0"
val imageIoExtensionVersion = "3.11.0"

inThisBuild(
  List(
    scalaVersion := usedScalaVersion,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
  )
)
