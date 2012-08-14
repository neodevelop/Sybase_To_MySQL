package mx.edu.ebc

class SybaseToMySQLOneTableWithoutKeyTests extends groovy.util.GroovyTestCase{
  
  def table1

  void setUp(){
    table1 = new TablaAMigrar()
    table1.tableName = "e_mov_prenom_his"
  }

  void testMigrate(){
   def result= table1.migrateTableWithoutKey()
   log.info("El resultado fue: $result")
  }
}