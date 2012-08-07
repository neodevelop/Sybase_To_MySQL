package mx.edu.ebc

class ReadingTablesFromFile extends GroovyTestCase {
  File file

  void setUp(){
    file = new File("${System.properties['user.home']}/tablas.txt")
  }

  void testGettingTables(){
    file.readLines().each{ name ->
      assert name.size()
    }
  }
}
