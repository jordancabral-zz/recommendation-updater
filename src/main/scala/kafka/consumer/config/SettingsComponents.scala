package kafka.consumer.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config
import java.util.Properties
import kafka.consumer.ConsumerConfig
import scala.collection.JavaConversions._

trait SettingsComponents {
  val settings = new Settings(ConfigFactory.load())
}

case class MongoSettings (mongoUri: String, mongoDbName: String, mongoCollection:String)
case class KafkaConsumerSettings(consumerConfig: ConsumerConfig, topics: List[TopicSettings])
case class TopicSettings(country: String, topic: String, actorCount: Int)

class Settings(config: Config){

  val enabledCountries = config.getStringList("enabled-countries").toList

  def kafkaConsumerSettingsFor(countryCode: String): KafkaConsumerSettings = {
    val consumerConfiguration = config.getConfig(s"${countryCode.toLowerCase()}.consumer")
    val topicList = consumerConfiguration.getConfigList("topics")
    KafkaConsumerSettings(new ConsumerConfig(kafkaProperties(consumerConfiguration)),
    topicList.toList.map(c => toTopicSettings(countryCode, c)))
  }

  private def toTopicSettings(country: String, config: Config): TopicSettings = {
    TopicSettings(country, config.getString("kafka_topic"), config.getInt("partitions"))
  }

  private def kafkaProperties(config: Config): Properties = {
    val props = new Properties

    props.setProperty("zk.connect", config.getString("zk.connect"))
    props.setProperty("groupid", config.getString("groupid"))
    props.setProperty("autocommit.enable", config.getBoolean("autocommit.enable").toString)
    props.setProperty("autocommit.interval.ms", config.getInt("autocommit.interval.ms").toString)
    props.setProperty("autooffset.reset", config.getString("autooffset.reset"))
    props.setProperty("zookeeper.session.timeout.ms", config.getInt("zookeeper.session.timeout.ms").toString)
    props.setProperty("zookeeper.sync.time.ms", config.getInt("zookeeper.sync.time.ms").toString)

    props
  }

  def mongoSettings: MongoSettings = {
    MongoSettings(config.getString("mongo.uri"), config.getString("mongo.dbName"), config.getString("mongo.collection"))
  }
}