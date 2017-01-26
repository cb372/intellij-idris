package cb372.intellij.idris.repl

import java.io._
import java.util.concurrent.atomic.AtomicInteger

import cb372.intellij.idris.repl.Response.Payload
import com.intellij.openapi.diagnostic.LoggerRt

import scala.util.{Failure, Success}

object IdrisRepl {

  private val log = LoggerRt.getInstance(getClass)

  private val requestCounter = new AtomicInteger(0)

  private val activeRequests = scala.collection.concurrent.TrieMap.empty[Int, ResponseListener]

  private def dispatchResponse(line: String): Unit = {
    new ResponseParser(line).Resp.run() match {
      case Success(response) =>
        log.info(s"Received response from Idris REPL: $response")
        val listener = activeRequests.get(response.requestNumber)
        listener.foreach { l =>
          response.payload match {
            case Payload.ProtocolVersion(_) => // don't care
            case Payload.SetPrompt(string) => l.setPrompt(string)
            case Payload.WriteString(string) => l.writeString(string)
          }
        }
        // TODO if it's an "OK" or "Error" response, remove the listener from the map
      case Failure(e) =>
        log.warn(s"Failed to parse Idris REPL response: $line", e)
    }

  }

  private val (process, stdin) = {
    val proc = new ProcessBuilder("idris", "--ide-mode").start()
    val stdin = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream))
    val stdout = new BufferedReader(new InputStreamReader(proc.getInputStream))
    val stderr = new BufferedReader(new InputStreamReader(proc.getErrorStream))
    new Thread("idris-repl-stdout") {
      override def run(): Unit = {
        var line: String = null
        while ({ line = stdout.readLine(); line } != null) {
          log.warn(s"Received from REPL: $line")
          dispatchResponse(line)
        }
      }
    }.start()
    new Thread("idris-repl-stderr") {
      override def run(): Unit = {
        var line: String = null
        while ({ line = stderr.readLine(); line } != null) {
          log.warn(s"Idris stderr: $line")
        }
      }
    }.start()
    (proc, stdin)
  }

  def loadFile(file: File, listener: ResponseListener): Unit = {
    val requestNumber = requestCounter.incrementAndGet()
    activeRequests.put(requestNumber, listener)
    val cmd = s"""(:load-file "${file.getCanonicalPath}")"""
    val request = RequestBuilder.toReplRequest(cmd, requestNumber)
    log.warn(s"Sending to REPL: $request")
    stdin.write(request)
    stdin.newLine()
    stdin.flush()
  }
}
