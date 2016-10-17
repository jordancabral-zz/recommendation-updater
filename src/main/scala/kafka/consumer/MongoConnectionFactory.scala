package kafka.consumer

import com.mongodb.casbah.{MongoDB, MongoClient}

object MongoConnectionFactory {
   def apply(client: MongoClient, dbName: String) = new MongoConnectionFactory(client,dbName)
}

class MongoConnectionFactory(client: MongoClient, dbName: String) {
  def db: MongoDB = client(dbName)

}
