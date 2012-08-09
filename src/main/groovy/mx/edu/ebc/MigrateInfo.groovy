package mx.edu.ebc

import java.text.SimpleDateFormat

class MigrateInfo {
  static File file
  static File fileResult

  MigrateInfo(){
    def dia = new Date()
    def formatter = new SimpleDateFormat("ddMMyyyyHHmmss")
    file = new File("${System.properties['user.home']}/tablas.txt")
    def nombreArchivo= formatter.format(dia) + ".txt"
    fileResult = new File("${System.properties['user.home']}/$nombreArchivo")
    fileResult.createNewFile()
  }

  def obtainTablesNamesFromFile(){
    file.readLines()
  }


  def synchronized saveResultToFile(stringResult) {
     fileResult.append(stringResult)
  }


}
