package la.smx.util.csv2lucene.ui

import java.io.File
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.{JPanel, JFileChooser, JFrame}

/**
 * Created by arjones on 7/11/15.
 */
object FileChooser {
  def getFile(): File = {
    val fc = new JFileChooser()
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY)
    fc.setFileFilter(new FileNameExtensionFilter("Comma Separated Value", "csv"))

    val file = if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(null)) {
      fc.getSelectedFile()

    } else {

      null
    }

    file
  }
}
