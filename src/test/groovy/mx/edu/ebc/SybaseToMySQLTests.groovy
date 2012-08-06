package org.edu.ebc

import groovy.sql.*

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
    def rowsPerTable = new DB().countRowsPerTable(tableNames)
    rowsPerTable.each { k,v ->
      log.info "$k tiene $v registros"
    }
    //def r = DB.withOneConnection { sql ->
    //  (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
    //}
    //log.info "${r}"
  }
}