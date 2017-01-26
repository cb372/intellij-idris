package cb372.intellij.idris.repl

case class Response(length: Int, payload: Response.Payload, requestNumber: Int)

object Response {

  sealed trait Payload

  object Payload {
    case class ProtocolVersion(version: String) extends Payload
    case class WriteString(string: String) extends Payload
    case class SetPrompt(string: String) extends Payload
  }

}

