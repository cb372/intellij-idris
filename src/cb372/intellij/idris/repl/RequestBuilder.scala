package cb372.intellij.idris.repl

object RequestBuilder {

  def toReplRequest(command: String, requestNumber: Int): String = {
    val withRequestNumber = s"($command $requestNumber)"
    s"${len(withRequestNumber)}$withRequestNumber"
  }

  private def len(string: String): String = {
    Integer.toHexString(string.length + 1).reverse.padTo(6, '0').reverse
  }

}
