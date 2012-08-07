package mx.edu.ebc

import java.sql.*
import groovy.util.logging.Log

@Log
class DBInfo {

  def countRowsPerTableNoCurry = { sql, tableName ->
    (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
  }

  def countRowsPerTable = countRowsPerTableNoCurry.curry(DB.instance.sqlSybase)

  def getTableNamesNoCurry = { sql ->
    def tableNames = []
    log.info "${sql.dump()}"
    DatabaseMetaData dbm = sql.connection.metaData
    //log.info dbm.properties
    def types = ["TABLE"]
    ResultSet rs = dbm.getTables(null,null,"%",types as String[])
    while (rs.next()){
      tableNames << rs.getString("TABLE_NAME")
    }
    tableNames
  }

}