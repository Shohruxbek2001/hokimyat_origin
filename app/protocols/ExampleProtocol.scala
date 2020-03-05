package protocols

import java.util.Date

import play.api.libs.json.{Json, OFormat}

object ExampleProtocol {

  case object GetList

  case class Delete(login: String)
  case class Create(data: Example)
  case class OnSubmit(login: String, password: String)

  case class Example(id: Option[Int] = None,
                     login: String,
                     password: String,
                     role: String,
                    )
  implicit val studentFormat: OFormat[Example] = Json.format[Example]
}

