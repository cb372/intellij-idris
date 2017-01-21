package cb372.intellij.idris.repl

import java.io._
import java.util.concurrent.atomic.AtomicInteger

import com.intellij.openapi.diagnostic.LoggerRt

object IdrisRepl {

  private val log = LoggerRt.getInstance(getClass)

  private val requestCounter = new AtomicInteger(0)

  private val activeRequests = scala.collection.concurrent.TrieMap.empty[Int, ResponseListener]

  private def dispatchResponse(line: String): Unit = {
    new ResponseParser(line)
    // TODO parse and dispatch to appropriate listener
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
    stdin.write(RequestBuilder.toReplRequest(cmd, requestNumber))
    stdin.newLine()
    stdin.flush()
  }
}
