name := "adie-akka-learning"

version := "1.0"

scalaVersion := "2.12.7"

lazy val akkaVersion = "2.5.20"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %%   "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "junit" % "junit" % "4.12",
  
  // Added for Akka Multi Cluster samples
  "com.typesafe.akka" %%   "akka-remote" % akkaVersion,
  "com.typesafe.akka" %%   "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %%   "akka-cluster-metrics" % akkaVersion,
  "com.typesafe.akka" %%   "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %%   "akka-multi-node-testkit" % akkaVersion
  //"org.scalatest" %% "scalatest" % "3.0.5" % Test, "io.kamon" % "sigar-loader" % "1.6.6-rev002"
  )
