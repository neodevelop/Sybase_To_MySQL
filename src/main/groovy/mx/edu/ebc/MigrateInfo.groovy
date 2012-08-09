package mx.edu.ebc

class MigrateInfo {
  static File file
  static File fileResult

  MigrateInfo(){
    file = new File("${System.properties['user.home']}/tablas.txt")
    fileResult = new File("${System.properties['user.home']}/resultado.txt")
  }

  def obtainTablesNamesFromFile(){
    file.readLines()
  }


  def synchronized saveResultToFile(stringResult) {
     fileResult.append(stringResult)
  }


}
