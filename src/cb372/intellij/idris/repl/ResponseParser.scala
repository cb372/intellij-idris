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

  def string: Rule1[String] = rule {
    ('"' ~ ('\\' ~ capture('"') | capture(!'"' ~ ANY)).* ~ '"') ~> ((xs: Seq[String]) => xs.mkString)
  }

  def writeString: Rule1[Payload.WriteString] = rule {
    ":write-string " ~ string ~> (s => Payload.WriteString(s)
    )
  }

  def setPrompt: Rule1[Payload.SetPrompt] = rule {
    ":set-prompt " ~ string ~> (s => Payload.SetPrompt(s))
  }

  def payload: Rule1[Payload] = rule { protocolVersion | writeString | setPrompt }

  def requestNum: Rule1[Int] = rule { capture(oneOrMore(Digit)) ~> ((string: String) => string.toInt) }

  def Resp: Rule1[Response] = rule {
    length ~ '(' ~ payload ~ ' ' ~ requestNum ~ ')' ~ EOI ~> ((l: Int, p: Payload, r: Int) => Response(l, p, r))
  }
}
