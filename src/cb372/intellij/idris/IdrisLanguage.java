package cb372.intellij.idris;

import com.intellij.lang.Language;

class IdrisLanguage extends Language {

  public static final IdrisLanguage INSTANCE = new IdrisLanguage();

  private IdrisLanguage() {
    super("Idris");
  }

}
