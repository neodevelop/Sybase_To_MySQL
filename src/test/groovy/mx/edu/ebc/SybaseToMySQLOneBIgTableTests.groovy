package mx.edu.ebc

class SybaseToMySQLOneBIgTableTests extends groovy.util.GroovyTestCase{
  
  def table1

  void setUp(){
    table1 = new TablaAMigrar()
    table1.tableName = "genexExamenText"
  }

  void testMigrateBigTable(){
    def result = table1.migrateBigTable()
    log.info("El resultado fue: $result")
  }
}