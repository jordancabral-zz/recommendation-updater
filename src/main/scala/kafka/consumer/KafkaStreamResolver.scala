package kafka.consumer

import kafka.consumer.config._
import kafka.serializer.DefaultDecoder
import scala.collection.mutable.HashMap

/**
 * Created by nzuidwijk on 3/11/14.
 */
trait KafkaStreamResolver extends SettingsComponents {

  val consumerConnector = Consumer.create(settings.kafkaConsumerSettingsFor("MX").consumerConfig)

  val topics = populateTopicConsumerMap(settings.kafkaConsumerSettingsFor("MX").topics)

  val topicStreamsMap = consumerConnector.createMessageStreams(topics, new DefaultDecoder())
//  val kafkaMessageStream = topicStreamsMap.get(topics.keysIterator.next()).get(0)

  def populateTopicConsumerMap(topicConfigList: List[TopicSettings]) = {
    val map = new HashMap[String, Int]()
    for (t <- topicConfigList) {
      map.put(t.topic, t.actorCount)
    }
    map
  }
}
