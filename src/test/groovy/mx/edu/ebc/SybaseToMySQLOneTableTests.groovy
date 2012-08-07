package mx.edu.ebc

class SybaseToMySQLOneTableTests extends groovy.util.GroovyTestCase{
  
  def table1

  void setUp(){
    table1 = new TablaAMigrar()
    table1.tableName = "acceso_gafete"
  }

  void testMigrate(){
    table1.migrate()
  }
}