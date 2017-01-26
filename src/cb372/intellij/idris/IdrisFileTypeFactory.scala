package cb372.intellij.idris

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class IdrisFileTypeFactory extends FileTypeFactory {
  def createFileTypes(fileTypeConsumer: FileTypeConsumer) {
    fileTypeConsumer.consume(IdrisFileType.Instance, "idr")
  }
}