import App.jsonFormats
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpMethods, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import org.json4s.native.Serialization.write

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}
import scala.util.Try

trait EveApiService {
  implicit def system: ActorSystem

  implicit def executionContext: ExecutionContext

  val httpRequestService: HttpRequestServiceImpl

  class HttpRequestServiceImpl {


    def sendGetRequest(url: String): Try[String] = Try {
      val req = HttpRequest(HttpMethods.GET, url)
      val resp = Http().singleRequest(req)
      val responseAsString = resp.flatMap { r => Unmarshal(r.entity).to[String] }
      val result: String = Await.result(responseAsString, Duration(40, TimeUnit.SECONDS))
      result
    }

    def sendPostRequest(url: String, listOfIds: Seq[Int]): Try[String] = Try {
      val req = HttpRequest(uri = url)
        .withMethod(HttpMethods.POST)
        .withEntity(ContentTypes.`application/json`, write(listOfIds.toSet.toList))
      val resp = Http().singleRequest(req)
      val responseAsString = resp.flatMap { r => Unmarshal(r.entity).to[String] }
      val result: String = Await.result(responseAsString, Duration(40, TimeUnit.SECONDS))
      result
    }
  }

}