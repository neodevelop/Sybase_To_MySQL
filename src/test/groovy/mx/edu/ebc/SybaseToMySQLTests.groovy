package mx.edu.ebc

class SybaseToMySQLTests extends groovy.util.GroovyTestCase {

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
    //def tableNames = dbInfo.tableNames
    def tableNames = ["acceso_gafete",
      "acceso_items",
      "acceso_motivo",
      //"acceso_persons",
      "acceso_rel_items_persons",
      "acceso_status",
      //"actividad",
      //"actividad_espacio",
      "actividad_subactividad",
      "actividades"]
    //def rowsPerTable = new DBInfo().countRowsPerTable(tableNames)
    //rowsPerTable.each { k,v ->
    // log.info "$k tiene $v registros"
    //}

    DB.withSybaseInstance { sql ->
      tableNames.each { tn ->
        def dbInfo = new DBInfo()
        def c = dbInfo.countRowsPerTable(tn)
        log.info "${c}"
      }
    }
    //def r = DB.withOneConnection { sql ->
    //  (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
    //}
    //log.info "${r}"
  }

}