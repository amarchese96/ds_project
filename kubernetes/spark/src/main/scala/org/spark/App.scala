package org.spark

import java.io.IOException

import com.sendgrid.{Method, Request, SendGrid}
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.{Content, Email}

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

import scala.collection.mutable._

object App {

  def sendEmail(message : String): Unit ={
    val from = new Email(System.getenv("EMAIL_SENDER"))
    val subject = "Spark statistics"
    val to = new Email(System.getenv("EMAIL_RECEIVER"))
    val content = new Content("text/plain", message)
    val mail = new Mail(from, subject, to, content)

    val sg = new SendGrid(System.getenv("SENDGRID_API_KEY"))
    val request = new Request()
    try {
      request.setMethod(Method.POST)
      request.setEndpoint("mail/send")
      request.setBody(mail.build)
      val response = sg.api(request)
    } catch {
      case ex: IOException =>
        println("Error in sending email")
    }
  }

  def main(args : Array[String]): Unit = {

    var previousBatchesQueue = Queue[Tuple2[Float,Float]]()
    val batchSize = System.getenv("BATCH_SIZE").toLong
    val previousBatchesNumber = System.getenv("PREVIOUS_BATCHES_NUMBER").toLong

    val conf = new SparkConf().setAppName("spark")
    val ssc = new StreamingContext(conf, Seconds(batchSize))

    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> System.getenv("KAFKA_ADDRESS"),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> System.getenv("KAFKA_GROUP_ID"),
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array(System.getenv("KAFKA_MAIN_TOPIC"))
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent, //Location strategy
      Subscribe[String, String](topics, kafkaParams)
    )

    val requests = stream.map(_.value.toFloat)
    val result = requests.map(x => (x, 1))
      .reduce((x, y) => (x._1 + y._1, x._2 + y._2))
      .map(x => (x._1/x._2, x._2.toFloat/batchSize.toFloat))

    result foreachRDD { rdd =>
      if(!rdd.isEmpty()){
        val currentBatch = rdd.collect()(0)
        println(s"Current RDD content:\n\n ${currentBatch}")

        if(!previousBatchesQueue.isEmpty){
          var x = 0.0
          var y = 0.0

          previousBatchesQueue.foreach{batch =>
            x += batch._1
            y += batch._2
          }

          x /= previousBatchesQueue.size
          y /= previousBatchesQueue.size

          val averageResponseTimeIncrement = (currentBatch._1 - x)/x
          val averageRequestsCountIncrement = (currentBatch._2 - y)/y

          if(averageResponseTimeIncrement > 0.20){
            if(averageRequestsCountIncrement > 0.20)
              this.sendEmail(s"Incremento del tempo medio di risposta pari a ${averageResponseTimeIncrement * 100.0}%. Registrato anche un incremento del numero medio di richieste pari a ${averageRequestsCountIncrement}%.")
            else
              this.sendEmail(s"Incremento del tempo medio di risposta pari a ${averageResponseTimeIncrement * 100.0}%. Non è stato registrato un incremento considerevole del numero medio di richieste.")
          }

        }


        previousBatchesQueue.enqueue(currentBatch)
        if(previousBatchesQueue.size > previousBatchesNumber)
          previousBatchesQueue.dequeue()

      }
    }

    ssc.start()
    ssc.awaitTermination()

  }

}
