package cb372.intellij.idris.client

import com.intellij.openapi.diagnostic.LoggerRt
import java.io.File
import java.io.IOException

object IdrisClient {

  private val log = LoggerRt.getInstance(getClass)

  def showType(file: File, expr: String): String = {
    try {
      reloadFile(file)
      sendCommand(":t " + expr)
    } catch {
      case e: IOException => "Failed to communicate with Idris REPL"
    }
  }

  private def reloadFile(file: File): String = sendCommand(":l " + file.getAbsolutePath)

  private def sendCommand(commands: String*): String = {
    import scala.sys.process._

    val command = Seq("idris", "--client") ++ commands
    log.warn("Sending command: " + command.mkString(" "))
    val output = command.!!
    log.warn("Command output: " + output)
    output
  }
}
