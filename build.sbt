import AssemblyKeys._

assemblySettings

mergeStrategy in assembly := {
  case "META-INF/MANIFEST.MF" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.SF" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.DSA" => MergeStrategy.discard
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.discard
  case "META-INF/*.SF" => MergeStrategy.discard
  case "META-INF/*.DSA" => MergeStrategy.discard
  case "META-INF/*.RSA" => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  // case "reference.conf" => MergeStrategy.last
  case _ => MergeStrategy.first
  //case _ => MergeStrategy.first
}

/*
[warn] Scala version was updated by one of library dependencies:
[warn]  * org.scala-lang:scala-compiler:2.10.0 -> 2.10.4
[warn] To force scalaVersion, add the following:
[warn]  ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
[warn] Run 'evicted' to see detailed eviction warnings

*/
ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

// dependency-tree
net.virtualvoid.sbt.graph.Plugin.graphSettings

organization  := "com.classcat"

version       := "0.1"

// scalaVersion  := "2.11.4"
scalaVersion  := "2.10.5"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.3.4"
  // val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  val sparkVersion = "1.4.1"
  // val sparkVersion = "1.3.0"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.7" % "test",
    "org.apache.spark"   %%  "spark-core"             % sparkVersion,
    "org.apache.spark"   %%  "spark-sql"              % sparkVersion,
    "joda-time" % "joda-time" % "2.8.1",
    "org.joda" % "joda-convert" % "1.7"
  )
}

Revolver.settings

Twirl.settings
