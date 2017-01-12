package cb372.intellij.idris.client;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.diagnostic.LoggerRt;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class IdrisClient {
  private IdrisClient() {}

  public static String showType(File file, String expr) {
    try {
      reloadFile(file);
      return sendCommand(":t " + expr);
    } catch (IOException e) {
      return "Failed to communicate with Idris REPL";
    }
  }

  private static String reloadFile(File file) throws IOException {
    return sendCommand(":l " + file.getAbsolutePath());
  }

  private static String sendCommand(String... commands) throws IOException {
    List<String> allArgs = new ArrayList<>();
    allArgs.add("idris");
    allArgs.add("--client");
    allArgs.addAll(Arrays.asList(commands));
    LoggerRt.getInstance(IdrisClient.class).warn("Sending command: " + allArgs);
    Process p = new ProcessBuilder(allArgs).start();
    try (BufferedReader outReader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
      String output = String.join(System.lineSeparator(), outReader.lines().collect(Collectors.toList()));
      LoggerRt.getInstance(IdrisClient.class).warn("Command output: " + output);
      return output;
    }
  }

}
