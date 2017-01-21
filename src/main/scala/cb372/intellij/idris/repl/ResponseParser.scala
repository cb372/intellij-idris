package cb372.intellij.idris.repl

import org.parboiled2._
import org.parboiled2.CharPredicate._
import Response._

class ResponseParser(val input: ParserInput) extends Parser {

  def length: Rule1[Int] = rule {
    capture(6.times(HexDigit)) ~> ((hexString: String) => Integer.parseInt(hexString, 16))
  }

  def protocolVersion: Rule1[Payload.ProtocolVersion] = rule {
    ":protocol-version " ~ capture((!" " ~ ANY).*) ~> (
      string => Payload.ProtocolVersion(string)
    )
  }

  def writeString: Rule1[Payload.WriteString] = rule {
     // TODO handle escaped double quotes
    ":write-string " ~ '"' ~ capture((!'"' ~ ANY).*)  ~ '"' ~> (
      string => Payload.WriteString(string)
    )
  }

  def payload: Rule1[Payload] = rule { protocolVersion | writeString }

  def requestNum: Rule1[Int] = rule { capture(oneOrMore(Digit)) ~> ((string: String) => string.toInt) }

  def Resp: Rule1[Response] = rule {
    length ~ '(' ~ payload ~ ' ' ~ requestNum ~ ')' ~ EOI ~> ((l: Int, p: Payload, r: Int) => Response(l, p, r))
  }
}
