lazy val akkaHttpVersion = "10.4.0"
lazy val akkaVersion    = "2.8.0"
lazy val slickVersion = "3.3.3"
lazy val postgresVersion = "42.6.2"

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

lazy val root = (project in file(".")).
  settings(
    organization := "com.example",
    scalaVersion := "2.12.18",
    name := "ecommerce-platform",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.11",
      "com.typesafe.slick" %% "slick" % slickVersion,
      "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
      "org.postgresql" % "postgresql" % postgresVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.12" % Test,
      "com.pauldijou" %% "jwt-core" % "4.3.0"
    )
  )
