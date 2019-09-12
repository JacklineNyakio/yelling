val kafkaVersion = "2.3.0"
val avroVersion  = "1.9.0"

lazy val sharedSettings = Seq(
  organization := "com.yelling",
  version      := "0.1.0",
  scalaVersion := "2.11.12",
  dependencyOverrides ++= Seq(
    "com.fasterxml.jackson.core"          % "jackson-core"               % "2.9.9",
    "com.fasterxml.jackson.core"          % "jackson-databind"           % "2.9.9",
    "com.fasterxml.jackson.module"        %% "jackson-module-scala"      % "2.9.9"
  ),
  resolvers ++= Seq(
    "Typesafe repository releases"           at "http://repo.typesafe.com/typesafe/releases/",
    "Confluent Maven Repository"             at "http://packages.confluent.io/maven/",
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/"
  )
)

lazy val root = (project in file(".")).aggregate(kafka, core)

val core = (project in file("core")).settings(
  sharedSettings,
  libraryDependencies ++= Seq (
    "com.typesafe"       %  "config"                              % "1.3.3"
  )
)

val kafka = (project in file("kafka")).settings(
  name := "Kafka Streaming Application",
  sharedSettings,
  libraryDependencies ++= Seq (
    "org.apache.avro"               %       "avro"                      %     avroVersion,
    "org.apache.avro"               %       "avro-tools"               %     avroVersion,
    "io.confluent"                  %       "kafka-avro-serializer"     %     "3.3.1",
    "com.sksamuel.avro4s"           %%      "avro4s-core"               %     "1.8.3" ,
    "org.scalaj"                    %%      "scalaj-http"               %     "2.4.2",
    "joda-time"                     %       "joda-time"                 %     "2.10.3",
    "org.joda"                      %       "joda-convert"              %     "1.8",
    "com.typesafe.scala-logging"    %%      "scala-logging"             %     "3.9.0",
    "org.slf4j"                     %       "slf4j-simple"              %     "1.7.25",
    "com.typesafe.scala-logging"    %%      "scala-logging"             %     "3.9.0",
    "org.apache.kafka"              %       "kafka-streams"             %     kafkaVersion,
    "org.apache.kafka"              %       "kafka-clients"             %     kafkaVersion
  )
).dependsOn(core)

