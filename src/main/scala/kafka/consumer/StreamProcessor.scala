package kafka.consumer

import kafka.consumer.config._
import kafka.message.Message
import java.nio.charset.{CharsetDecoder, Charset}
import org.json4s.jackson.JsonMethods._
import org.json4s.DefaultFormats
import scala.annotation.tailrec


/**
 * Created by nzuidwijk on 3/11/14.
 */
class StreamProcessor(iterator: ConsumerIterator[Message]) extends Runnable with SettingsComponents with JobSeekerRecommendationUpdater{
  val decoder = Charset.forName("UTF-8").newDecoder()

  @tailrec //tailrec le indica al compilador que tome la funcion recursiva como un while
  final def execute(iterator: ConsumerIterator[Message]){
    if (iterator.hasNext()){
      val event = new MessageParser(iterator.next, decoder).parseMessage()
      if (event.eventType == "POSTULATION") updateRecomendations(event)
      execute(iterator)
    }

  }

  override def run(): Unit =  this.execute(iterator)
}

class MessageParser(message: Message, decoder : CharsetDecoder){
  implicit val formats = DefaultFormats

  def parseMessage() : Event = {
    val jsonString = decoder.decode(message.payload).toString()
    //jsonString
    val jsonObject = parse(jsonString)
    jsonObject.extract[Event]
  }
}

//eventType: POSTULATION
//uid: postulationId
case class Event(eventType: String, uid: Long, eventData: EventData)

case class EventData(id: Long, jobSeeker:JobSeeker, jobApplications: List[Job])

case class JobSeeker(id: String)

case class Job(jobId: Long)