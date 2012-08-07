package mx.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class DBInfo {

  def countRowsPerTableNoCurry = { sql, tableName ->
    (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
  }

  def countRowsPerTable = countRowsPerTableNoCurry.curry(DB.sqlSybase)

  def getTableNamesNoCurry = { sql ->
    def tableNames = []
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