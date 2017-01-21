package cb372.intellij.idris.repl

import org.scalatest._
import Response.Payload._

import scala.util.Success

class ResponseParserSpec extends FlatSpec with Matchers {

// Example responses:
//000018(:protocol-version 1 0)
//000041((:load-file "/Users/chris/IdeaProjects/untitled/src/foo.idr") 1)
//000051(:write-string "Type checking /Users/chris/IdeaProjects/untitled/src/foo.idr" 1)
//00032c(:output (:ok (:highlight-source ((((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 1 17) (:end 1 20)) ((:name "Prelude.Nat.Nat") (:implicit :False) (:decor :type) (:doc-overview "Natural numbers: unbounded, unsigned integers
//which can be pattern matched.") (:type "Type") (:namespace "Prelude.Nat"))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 1 17) (:end 1 20)) ((:name "Prelude.Nat.Nat") (:implicit :False) (:decor :type) (:doc-overview "Natural numbers: unbounded, unsigned integers
//which can be pattern matched.") (:type "Type") (:namespace "Prelude.Nat"))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 1 7) (:end 1 13)) ((:decor :type) (:type "Type") (:doc-overview "Strings in some unspecified encoding") (:name "String")))))) 1)
//0000fb(:output (:ok (:highlight-source ((((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 1 1) (:end 1 4)) ((:name "Main.len") (:implicit :False) (:decor :function) (:doc-overview "") (:type "String -> Nat") (:namespace "Main")))))) 1)
//000258(:output (:ok (:highlight-source ((((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 5) (:end 2 6)) ((:name "x") (:decor :bound) (:implicit :False))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 1) (:end 2 4)) ((:name "Main.len") (:implicit :False) (:decor :function) (:doc-overview "") (:type "String -> Nat") (:namespace "Main"))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 1) (:end 2 4)) ((:name "Main.len") (:implicit :False) (:decor :function) (:doc-overview "") (:type "String -> Nat") (:namespace "Main")))))) 1)
//00035c(:output (:ok (:highlight-source ((((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 16) (:end 2 17)) ((:name "x") (:decor :bound) (:implicit :False))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 16) (:end 2 17)) ((:name "x") (:decor :bound) (:implicit :False))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 9) (:end 2 15)) ((:name "Prelude.Strings.length") (:implicit :False) (:decor :function) (:doc-overview "Returns the length of the string.") (:type "String -> Nat") (:namespace "Prelude.Strings"))) (((:filename "/Users/chris/IdeaProjects/untitled/src/foo.idr") (:start 2 9) (:end 2 15)) ((:name "Prelude.Strings.length") (:implicit :False) (:decor :function) (:doc-overview "Returns the length of the string.") (:type "String -> Nat") (:namespace "Prelude.Strings")))))) 1)
//00003e(:set-prompt "*/Users/chris/IdeaProjects/untitled/src/foo" 1)
//000015(:return (:ok ()) 1)

  it should "parse a protocol-version response" in {
    val result = new ResponseParser("000018(:protocol-version 1 0)").Resp.run()
    result should be(
      Success(Response(24, ProtocolVersion("1"), 0)))
  }

  it should "parse a write-string response" in {
    new ResponseParser("""000051(:write-string "Type checking /Users/chris/IdeaProjects/untitled/src/foo.idr" 1)""").Resp.run() should be(
      Success(Response(81, WriteString("Type checking /Users/chris/IdeaProjects/untitled/src/foo.idr"), 1)))
  }

}
