package mx.edu.ebc

class MigrateInfo {
  static File file

  MigrateInfo(){
    file = new File("${System.properties['user.home']}/tablas.txt")
  }

  def obtainTablesNamesFromFile(){
    file.readLines()
  }

}
