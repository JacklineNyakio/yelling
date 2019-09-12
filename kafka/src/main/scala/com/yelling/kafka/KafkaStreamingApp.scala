package com.yelling.kafka

import java.util.Properties

import scala.util.{ Success, Failure }

import com.typesafe.scalalogging.LazyLogging

import org.apache.kafka._
import common.serialization.Serdes
import streams.{ StreamsConfig, StreamsBuilder, KafkaStreams, Topology }
import streams.kstream.{ KStream, ValueMapper }

object KafkaStreamingApp extends App with LazyLogging {
  val props = new Properties()

//  Defining the configuration items.
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "yelling_streaming_application")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, classOf[Serdes])
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[Serdes])

//  create a StreamBuilder instance to construct the processor topology.
  val builder = new StreamsBuilder()

  val topology : Topology = builder.build()

//  Defining the source for the stream(Source processor node)
  val  yellingStreamSource: KStream[String, String] = builder.stream("randomWords")

//  Processor node 2 --> Upper case processor
    val upperCased: KStream[String, String] = yellingStreamSource.mapValues[String](
      new ValueMapper[String, String]() {
        override def apply(value: String) : String = {
          value.toUpperCase
        }
    }
  )

//  write transformed value to sink

  upperCased.to("capitalizedRandomWords-topic")

//  create an instance of a kafka stream
  val kafkaStreams : KafkaStreams = new KafkaStreams(topology, props)

  kafkaStreams.start()

}