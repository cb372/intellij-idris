package cb372.intellij.idris.repl

import org.scalatest._

class RequestBuilderSpec extends FlatSpec with Matchers {

  it should "build a valid Idris IDE protocol request" in {
    RequestBuilder.toReplRequest("""(:load-file "/tmp/foo")""", 123) should be(
      """00001e((:load-file "/tmp/foo") 123)""" // 30 chars including newline
    )
  }

}
