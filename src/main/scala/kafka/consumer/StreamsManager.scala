package kafka.consumer

import kafka.message.Message

/**
 * Created by nzuidwijk on 3/11/14.
 */
trait StreamsManager extends KafkaStreamResolver {

  val allStreamsQuantity = topicStreamsMap.foldLeft(0)((accumulated, keyValue: (String,List[KafkaMessageStream[Message]])) => accumulated + keyValue._2.length )
  val pool = java.util.concurrent.Executors.newFixedThreadPool(allStreamsQuantity)
  println("allStreamsQuantity=" + allStreamsQuantity)

  for ((topicName, kafkaMessageStreamList) <- topicStreamsMap) {
    for (stream <- kafkaMessageStreamList) {
      pool.execute(new StreamProcessor(stream.iterator()))
    }
  }

}
