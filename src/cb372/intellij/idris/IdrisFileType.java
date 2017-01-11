package cb372.intellij.idris;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class IdrisFileType extends LanguageFileType {

  public static final IdrisFileType INSTANCE = new IdrisFileType();

  private IdrisFileType() {
    super(IdrisLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Idris file";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Idris file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "idr";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return IdrisIcons.FILE;
  }

}
