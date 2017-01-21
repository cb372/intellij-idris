package cb372.intellij.idris

import com.intellij.lang.Language

object IdrisLanguage {
  val Instance = new IdrisLanguage
}

class IdrisLanguage private() extends Language("Idris")