package org.edu.ebc

class SybaseToMySQLTests extends GroovyTestCase {

  def currentTable
  def dbInfo

  void setUp(){
    currentTable = new TablaAMigrar()
    currentTable.tableName = "areaesc"
    dbInfo = new DBInfo()
  }

  void testCountRowsInTable(){
    assertNotNull currentTable
    assert currentTable.count() > 0
  }

  void testCountRowsForAllTables(){
    def tableNames = dbInfo.tableNames
    tableNames.each{ tableName ->
      def thisTable = new TablaAMigrar()
      thisTable.tableName = tableName
      log.info "${thisTable.count()}"
    }
  }
}