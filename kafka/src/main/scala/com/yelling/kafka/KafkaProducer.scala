package com.yelling.kafka

import java.util.Properties

import scala.io.StdIn

import org.apache.kafka._
import clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import common.serialization.StringSerializer

import com.yelling.core.util.YellingConfig.KafkaConfig

object RandomWordsProducer {
  val topic = "randomWords"

  val props = new Properties()

  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
    KafkaConfig.kafkaBroker)
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    classOf[StringSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    classOf[StringSerializer])
  props.put(ProducerConfig.CLIENT_ID_CONFIG,
    topic)

  def main(args: Array[String]): Unit = {
    val source = List("cow", "goat", "Sheep", "cups", "spoons", "chairs", "carpet", "etc")


    def writeToKafka(key: String, value: String): Unit = {
      val producer = new KafkaProducer[String, String](props)

      val producerRecord = new ProducerRecord[String, String](
        topic,
        key,
        value
      )

      val callback = new Callback {
        override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
          if (exception == null)
            println("The  data was written to kafka successfully.")
          else
            println(s"Failed to write the data successfully to kafka $exception")
        }
      }
      producer.send(producerRecord, callback)
    }

    source.foreach(word =>
      writeToKafka(
        topic,
        word
      ))
    StdIn.readLine()
  }
}
