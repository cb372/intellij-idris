package cb372.intellij.idris.repl

trait ResponseListener {

  def writeString(string: String): Unit

}
