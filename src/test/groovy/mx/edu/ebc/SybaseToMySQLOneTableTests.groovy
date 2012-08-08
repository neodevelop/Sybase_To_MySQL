package mx.edu.ebc

class SybaseToMySQLOneTableTests extends groovy.util.GroovyTestCase{
  
  def table1

  void setUp(){
    table1 = new TablaAMigrar()
    table1.tableName = "genexExamenText"
  }

  void testMigrate(){
   def result= table1.migrate()
   log.info("El resultado fue: $result")
  }
}