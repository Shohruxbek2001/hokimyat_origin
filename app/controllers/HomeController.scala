package controllers

import java.sql.Date

import akka.actor.ActorRef
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import akka.pattern.ask
import javax.inject._
import play.api.mvc._
import org.webjars.play.WebJarsUtil
import play.api.libs.json.Json
import protocols.ExampleProtocol._
import protocols.TadbirProtocol._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
object HomeController {

  case class User(login: String, password: String)

  var usersList = List(
    User("admin", "admin123"),
    User("user", "user123"),
  )
}

@Singleton
class HomeController @Inject()(val controllerComponents: ControllerComponents,
                              @Named("manager") val manager: ActorRef,
                               implicit val webJarsUtil: WebJarsUtil,
                               val indexPage: views.html.index,
                               val monitoringPage: views.html.monitoring
                              )
                              (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  import HomeController._

  val LoginSessionKey = "login.key"

  def monitoring: Action[AnyContent] = Action {
    Ok(monitoringPage(true))
  }

  def index: Action[AnyContent] = Action { implicit request: RequestHeader => {
    try {
      val result = request.session.get(LoginSessionKey)
      if (result.isDefined) {
        Ok(monitoringPage(true))
      }
      else
        Ok(indexPage(false))
    } catch {
      case e: Exception =>
        println(e.toString)
        Redirect("/").withNewSession
    }
  }
  }

  def loginPost = Action.async(parse.json) { implicit request => {
    val login = (request.body \ "login").as[String]
    val password = (request.body \ "password").as[String]
    (manager ? OnSubmit(login, password)).mapTo[Option[Example]].map {
      case Some(sss) =>
        Redirect(routes.HomeController.monitoring()).addingToSession(LoginSessionKey -> login)

      case None =>
        Redirect(routes.HomeController.monitoring)

    }
    }
  }
  def addPost = Action.async(parse.json) { implicit request => {
    val login = (request.body \ "login").as[String]
    val password = (request.body \ "password").as[String]
    val role = (request.body \ "role").as[String]
    (manager ? Create(Example(None, login, password, role))).mapTo[Int].map{id =>
      Ok(Json.toJson(s"$id idli user qoshildi"))
    }
  }}

  def deletePost = Action.async(parse.json) { implicit request => {
    val login = (request.body \ "login").as[String]
    val password = (request.body \ "password").as[String]
    val role = (request.body \ "role").as[String]
    (manager ? Delete(login)).mapTo[Int].map{id =>
      if (id == 0) {
        Ok(Json.toJson(s"$login unday user mavjud emas"))
      }
      else {
        Ok(Json.toJson(s"$login user ochirildi"))
      }
    }
  }
  }

  def adddataPost = Action.async(parse.json) { implicit request => {
    val hudud = (request.body \ "hudud").as[String]
    val rahbar = (request.body \ "rahbar").as[String]
    val date = (request.body \ "date").as[Date]
    val text = (request.body \ "text").as[String]
    val value = (request.body \ "value").as[String]
    (manager ? Createdata(Tadbir(None, hudud, rahbar, date, text, value))).mapTo[Int].map{ id =>
      Ok(Json.toJson(s"$id idli tadbir qoshildi"))
    }
  }}

  def deletedataPost = Action.async(parse.json) { implicit request => {
    val hudud = (request.body \ "hudud").as[String]
    val rahbar = (request.body \ "rahbar").as[String]
    val date = (request.body \ "date").as[Date]
    (manager ? Deletedata(hudud: String, rahbar: String, date: Date)).mapTo[Int].map{id =>
      if (id == 0) {
        Ok(Json.toJson(s"$hudud tadbir mavjud emas"))
      }
      else {
        Ok(Json.toJson(s"$hudud tadbir ochirildi"))
      }
    }
  }
  }

  def changedataPost = Action.async(parse.json) { implicit request => {
    val hudud = (request.body \ "hudud").as[String]
    val rahbar = (request.body \ "rahbar").as[String]
    val date = (request.body \ "date").as[Date]
    val text = (request.body \ "text").as[String]
    val value = (request.body \ "value").as[String]
    (manager ? Updatedata(Tadbir(None, hudud, rahbar, date, text, value))).mapTo[Int].map{ id =>
      if (id == 0) {
        Ok(Json.toJson(s"$hudud tadbir mavjud emas"))
      }
      else {
        Ok(Json.toJson(s"$hudud tadbir ochirildi"))
      }
    }
  }
  }

  }

