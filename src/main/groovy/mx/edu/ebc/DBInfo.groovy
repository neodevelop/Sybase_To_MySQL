package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class DBInfo {

  def sqlSybase

  def countRowsPerTableNoCurry = { sql, tableName ->
    (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
  }

  def countRowsPerTable = countRowsPerTableNoCurry.curry(DB.sqlSybase)

  def getTableNames(){
    def tableNames = []
    DatabaseMetaData dbm = sqlSybase.connection.metaData
    //log.info dbm.properties
    def types = ["TABLE"]
    ResultSet rs = dbm.getTables(null,null,"%",types as String[])
    while (rs.next()){
      tableNames << rs.getString("TABLE_NAME")
    }
    tableNames
  }

  def countRowsPerTablesNames(tableNames){
    def result = [:]
    tableNames.each{ tableName ->
      result."$tableName" = countRows(tableName)
    }
    result
  }

  private def countRows(tableName){
    (sqlSybase.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
  }

}