package com.yelling.core
package util

import com.typesafe.config.ConfigFactory

object YellingConfig {
  val config = ConfigFactory.load()

  object KafkaConfig {
    val kafkaBroker  = config.getString("yelling.kafka.brokers")
    val kafkaOffSetReset = config.getString("yelling.kafka.offsetReset")
  }
}