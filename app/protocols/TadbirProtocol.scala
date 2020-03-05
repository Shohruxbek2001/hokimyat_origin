package protocols

import java.sql.Date

import play.api.libs.json.{Json, OFormat}

object TadbirProtocol {

  case object GetList

  case class Deletedata(hudud: String, rahbar: String, date: Date)
  case class Updatedata(data: Tadbir)
  case class Createdata(data: Tadbir)

  case class Tadbir(id: Option[Int] = None,
                     hudud: String,
                     rahbar: String,
                     date: Date,
                     text: String,
                     value: String,
                    )
  implicit val studentFormat: OFormat[Tadbir] = Json.format[Tadbir]
}

