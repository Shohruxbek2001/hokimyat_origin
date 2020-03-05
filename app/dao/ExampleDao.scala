package dao

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.ExampleProtocol.Example
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait ExampleComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class ExampleTable(tag: Tag) extends Table[Example](tag, "user") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def login = column[String]("login")
    def password = column[String]("password")
    def role = column[String]("role")

    def * = (id.?, login, password, role) <> (Example.tupled, Example.unapply _)
  }

}

@ImplementedBy(classOf[ExampleDaoImpl])
trait ExampleDao {
  def create(data: Example): Future[Int]

  def getAll: Future[Seq[Example]]

  def delete(login: String): Future[Int]

  def update(data: Example): Future[Int]

  def onsubmit(login: String, password: String): Future[Option[Example]]


}

@Singleton
class ExampleDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends ExampleDao
    with ExampleComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val examplesTable = TableQuery[ExampleTable]

  override def create(data: Example): Future[Int] = {
    db.run {
      logger.warn(s"daoga keldi: $data")
      (examplesTable returning examplesTable.map(_.id)) += data
    }
  }

  override def getAll: Future[Seq[Example]] = {
    db.run {
      examplesTable.result
    }
  }

  override def delete(login: String): Future[Int] = {
    db.run {
      examplesTable.filter(_.login === login).delete
    }
  }

  override def update(data: Example): Future[Int] = {
    db.run {
      examplesTable.filter(_.login === data.login).update(data)
    }
  }

  override def onsubmit(login: String, password: String): Future[Option[Example]] = {
    db.run {
      examplesTable.filter(col => col.login === login && col.password === password).result.headOption
    }
  }
}

