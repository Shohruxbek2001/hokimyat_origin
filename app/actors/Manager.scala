package actors

import java.sql.Date

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.ExampleDao
import dao.TadbirDao
import javax.inject.Inject
import play.api.Environment
import protocols.ExampleProtocol.{GetList, _}
import protocols.TadbirProtocol._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class Manager @Inject()(val environment: Environment,
                        exampleDao: ExampleDao,
                        tadbirDao: TadbirDao
                              )
                       (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case Create(data) =>
      log.warning(s"menagerga keldi: $data")
      create(data).pipeTo(sender())

    case Delete(login) =>
      delete(login).pipeTo(sender())

    case OnSubmit(login, password) =>
      onsubmit(login, password).pipeTo(sender())

    case Createdata(data) =>
      createdata(data).pipeTo(sender())

    case Deletedata(hudud, rahbar, date) =>
      deletedata(hudud, rahbar, date).pipeTo(sender())

    case GetList =>
      getList.pipeTo(sender())

    case GetList =>
      getdataList.pipeTo(sender())

    case _ => log.info(s"received unknown message")
  }

  private def create(data: Example): Future[Int] = {
    log.warning(s"daoga yuborildi: $data")
    exampleDao.create(data)
  }

  private def getList: Future[Seq[Example]] = {
    exampleDao.getAll
  }

  private def getdataList: Future[Seq[Tadbir]] = {
    tadbirDao.getAll
  }

  private def delete(login: String): Future[Int] = {
    exampleDao.delete(login)
  }

  private def onsubmit(login: String, password: String): Future[Option[Example]] = {
    exampleDao.onsubmit(login, password)
  }

  private def deletedata(hudud: String, rahbar: String, date: Date): Future[Int] = {
    tadbirDao.delete(hudud, rahbar, date)
  }

  private def createdata(data: Tadbir): Future[Int] = {
    log.warning(s"daoga yuborildi: $data")
    tadbirDao.create(data)
  }

}
