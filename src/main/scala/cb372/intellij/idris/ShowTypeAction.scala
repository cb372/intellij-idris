package cb372.intellij.idris

import com.intellij.codeInsight.hint.HintManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

import cb372.intellij.idris.client.IdrisClient

class ShowTypeAction extends AnAction {
  def actionPerformed(e: AnActionEvent) {
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    if (project != null && editor != null) {
      val vf: VirtualFile = FileDocumentManager.getInstance.getFile(editor.getDocument)
      if (vf != null && vf.isInLocalFileSystem) {
        val textToQuery: String = findTextToQuery(editor)
        val response: String = IdrisClient.showType(new File(vf.getCanonicalPath), textToQuery)
        HintManager.getInstance.showInformationHint(editor, response)
      }
    }
  }

  private def findTextToQuery(editor: Editor): String = {
    // get the selected text if there is any, otherwise use the word under the cursor
    if (editor.getSelectionModel.hasSelection) {
      editor.getSelectionModel.getSelectedText
    } else {
      val offset: Int = editor.getCaretModel.getOffset
      val document: Document = editor.getDocument
      val chars: CharSequence = document.getImmutableCharSequence
      findWordUnderCursor(chars, offset)
    }
  }

  private def findWordUnderCursor(chars: CharSequence, caretOffset: Int): String = {
    val sb: StringBuilder = new StringBuilder
    var offset: Int = caretOffset
    while (offset > 0 && isValidInIdentifier(chars.charAt(offset - 1))) {
      offset -= 1
    }
    while (offset < chars.length && isValidInIdentifier(chars.charAt(offset))) {
      sb.append(chars.charAt(offset))
      offset += 1
    }
    sb.toString
  }

  private def isValidInIdentifier(c: Char): Boolean = {
    // TODO this is probably far too restrictive.
    // Haskell supports unicode identifiers, so Idris probably does too.
    Character.isLetterOrDigit(c) || c == '_' || c == '\''
  }
}