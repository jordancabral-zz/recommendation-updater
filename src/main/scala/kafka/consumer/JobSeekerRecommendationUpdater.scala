package kafka.consumer

import kafka.consumer.config.SettingsComponents
import org.json4s.DefaultFormats
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.query.Imports
import com.mongodb.casbah.{MongoClientURI, MongoClient}

/**
 * Created by jordan on 3/14/14.
 */
trait JobSeekerRecommendationUpdater extends SettingsComponents{
  implicit val formats = DefaultFormats

  val mongoSettings	= settings.mongoSettings

  val mongoClient = MongoClient(MongoClientURI(mongoSettings.mongoUri))
  val connectionFactory = MongoConnectionFactory(mongoClient,mongoSettings.mongoDbName)
  def recommendationsCollection: MongoCollection = connectionFactory.db(mongoSettings.mongoCollection)

  def updateRecomendations(event: Event) {
    println("EventType: " + event.eventType + ". PostulationId: " + event.uid + " JobSeekerId: " + event.eventData.jobSeeker.id)
    //    println("JobId: " + event.eventData.jobApplications.map(_.jobId))
    val query = MongoDBObject("u" -> event.eventData.jobSeeker.id.toInt)
    val inExpression: Imports.DBObject = "j" $in(event.eventData.jobApplications.map(_.jobId))
    recommendationsCollection.update(query, $pull("ranked_jobs" -> inExpression), false, false)
  }

  def shutdown() {
    mongoClient.close()
  }
}
