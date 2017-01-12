package cb372.intellij.idris;

import cb372.intellij.idris.client.IdrisClient;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

public class ShowTypeAction extends AnAction {

  @Override
  public void actionPerformed(AnActionEvent e) {
    final Project project = e.getData(CommonDataKeys.PROJECT);
    final Editor editor = e.getData(CommonDataKeys.EDITOR);
    if (project != null && editor != null) {
      final VirtualFile vf = FileDocumentManager.getInstance().getFile(editor.getDocument());
      if (vf != null && vf.isInLocalFileSystem()) {
        final String textToQuery = findTextToQuery(editor);
        final String response = IdrisClient.showType(new File(vf.getCanonicalPath()), textToQuery);
        HintManager.getInstance().showInformationHint(editor, response);
      }
    }
  }

  private String findTextToQuery(Editor editor) {
    // get the selected text if there is any, otherwise use the word under the cursor
    if (editor.getSelectionModel().hasSelection()) {
      return editor.getSelectionModel().getSelectedText();
    } else {
      int offset = editor.getCaretModel().getOffset();
      Document document = editor.getDocument();
      CharSequence chars = document.getImmutableCharSequence();
      return findWordUnderCursor(chars, offset);
    }
  }

  private String findWordUnderCursor(CharSequence chars, int caretOffset) {
    StringBuilder sb = new StringBuilder();
    int offset = caretOffset;
    while (offset > 0 && isValidInIdentifier(chars.charAt(offset - 1))) {
      offset -= 1;
    }
    while (offset < chars.length() && isValidInIdentifier(chars.charAt(offset))) {
      sb.append(chars.charAt(offset));
      offset += 1;
    }
    return sb.toString();
  }

  private boolean isValidInIdentifier(char c) {
    // TODO this is probably far too restrictive.
    // Haskell supports unicode identifiers, so Idris probably does too.
    return Character.isLetterOrDigit(c) || c == '_' || c == '\'';
  }

}
