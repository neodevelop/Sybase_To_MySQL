package mx.edu.ebc

class SybaseToMySQLOneBIgTableTests extends groovy.util.GroovyTestCase{
  
  def table1

  void setUp(){
    table1 = new TablaAMigrar()
    table1.tableName = "cuentas"
  }

  void testMigrateBigTable(){
    table1.migrateBigTable()
  }
}