package mx.edu.ebc

import java.text.SimpleDateFormat

class MigrateInfo {
  static File file
  static File fileResult
  static File partialResult
  static  File partialResultColumns
  def formatter = new SimpleDateFormat("ddMMyyyyHHmmss")

  MigrateInfo(){
    file = new File("${System.properties['user.home']}/tablas.txt")
    def dia = new Date()
    def nombreArchivo= "resultado"+formatter.format(dia) + ".txt"
    fileResult = new File("${System.properties['user.home']}/$nombreArchivo")
    fileResult.createNewFile()
  }

  def obtainTablesNamesFromFile(){
    file.readLines()
  }

  def obtainTablesNamesAndColumnFromFile(){
      File fileTableAndColumns
      fileTableAndColumns = new File("${System.properties['user.home']}/tablasColumnas.txt")
      fileTableAndColumns.readLines()
  }

  def createPartialResultFile() {
      def dia = new Date()
      def nombreArchivo= "resultadoParcial"+formatter.format(dia) + ".txt"
      partialResult = new File("${System.properties['user.home']}/$nombreArchivo")
      partialResult.createNewFile()
  }


  def createNewResultFile() {
      def dia = new Date()
      def nombreArchivo= "resultado"+formatter.format(dia) + ".txt"
      fileResult = new File("${System.properties['user.home']}/$nombreArchivo")
      fileResult.createNewFile()
  }
  def createPartialResultByColumnsFile() {
        def dia = new Date()
        def nombreArchivo= "resultadoParcialColumnas"+formatter.format(dia) + ".txt"
      partialResultColumns = new File("${System.properties['user.home']}/$nombreArchivo")
      partialResultColumns.createNewFile()
  }

  def synchronized saveResultToFile(stringResult) {
     fileResult.append(stringResult)
  }

  def synchronized savePartialResultToFile(stringResult) {
      partialResult.append(stringResult)
  }

  def synchronized savePartialResultByColumnsToFile(stringResult) {
      partialResultColumns.append(stringResult)
    }

}
