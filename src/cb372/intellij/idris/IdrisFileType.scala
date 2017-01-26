package cb372.intellij.idris

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing._

object IdrisFileType {
  val Instance = new IdrisFileType
}

class IdrisFileType private() extends LanguageFileType(IdrisLanguage.Instance) {
  def getName: String = "Idris file"
  def getDescription: String = "Idris file"
  def getDefaultExtension: String = "idr"
  def getIcon: Icon = IdrisIcons.File
}