package dao

import java.sql.Date

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.TadbirProtocol.Tadbir
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait TadbirComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class TadbirTable(tag: Tag) extends Table[Tadbir](tag, "tadbir") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def hudud = column[String]("hudud")
    def rahbar = column[String]("rahbar")
    def date = column[Date]("date")
    def text = column[String]("text")
    def value = column[String]("value")

    def * = (id.?, hudud, rahbar, date, text, value) <> (Tadbir.tupled, Tadbir.unapply _)
  }

}

@ImplementedBy(classOf[TadbirDaoImpl])
trait TadbirDao {
  def create(data: Tadbir): Future[Int]

  def getAll: Future[Seq[Tadbir]]
  def getAllWithFilter(hudud: Set[String]): Future[Seq[Tadbir]]
    /*shu yerda savol*/
  def delete(hudud: String, rahbar: String, date: Date): Future[Int]

  def update(data: Tadbir): Future[Int]
}

@Singleton
class TadbirDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends TadbirDao
    with TadbirComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val TadbirlarTable = TableQuery[TadbirTable]

  override def create(data: Tadbir): Future[Int] = {
    db.run {
      logger.warn(s"daoga keldi: $data")
      (TadbirlarTable returning TadbirlarTable.map(_.id)) += data
    }
  }

  override def getAll: Future[Seq[Tadbir]] = {
    db.run {
      TadbirlarTable.result
    }
  }

  override def getAllWithFilter(hudud: Set[String]): Future[Seq[Tadbir]] = {
    db.run {
      TadbirlarTable.filter(_.hudud inSet hudud).result
    }
  }

  override def delete(hudud: String, rahbar: String, date: Date): Future[Int] = {
    db.run {
      TadbirlarTable.filter(t => t.hudud === hudud && t.rahbar === rahbar && t.date === date ).delete
    }
  }

  override def update(data: Tadbir): Future[Int] = {
    db.run {
      TadbirlarTable.filter(b => b.hudud === data.hudud && b.rahbar === data.rahbar && b.date === data.date ).update(data)
    }
  }
}

