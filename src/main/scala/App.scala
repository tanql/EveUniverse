import akka.actor.ActorSystem
import org.json4s.{DefaultFormats, Formats}
import org.json4s.jackson.Serialization

import scala.concurrent.ExecutionContext
import scala.util.{Success, Try}

object App extends App with EveApiService {


  implicit val system = ActorSystem("EveApp")
  implicit val executionContext: ExecutionContext = system.dispatcher

  implicit val jsonFormats: Formats = {
    DefaultFormats
  }

  override val httpRequestService = new HttpRequestServiceImpl


  private def printAllUniverseNames(): Unit ={
    val allOrders = httpRequestService.sendGetRequest("https://esi.evetech.net/latest/markets/10000002/orders/?datasource=tranquility&order_type=all&page=1")
    val typeIdsOfOrders = handleResponseFromApi[Order](allOrders).map(_.type_id)
    val findUniverses  = httpRequestService.sendPostRequest("https://esi.evetech.net/latest/universe/names/?datasource=tranquility",typeIdsOfOrders)
    val universeNames = handleResponseFromApi[Universe](findUniverses).map(_.name)
    print(universeNames)
  }

  private def handleResponseFromApi[E](json: Try[String])(implicit m: Manifest[E]):Seq[E] = {
    json match {
      case Success(result) => {
        fromJson[Seq[E]](result)

      }
      case _ => Seq.empty
    }
  }

  private def fromJson[E](json: String)(implicit m: Manifest[E]): E = {
    Serialization.read[E](json)
  }

  printAllUniverseNames
}
