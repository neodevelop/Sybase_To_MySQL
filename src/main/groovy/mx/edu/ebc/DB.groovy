package org.edu.ebc

import groovy.sql.*

class DB{

  def sqlSybase

  DB(){
    sqlSybase = Sql.newInstance(
      DBParameters.SYBASE_PARAMS.url,
      DBParameters.SYBASE_PARAMS.user,
      DBParameters.SYBASE_PARAMS.password,
      DBParameters.SYBASE_PARAMS.driver
    )
  }

  def countRowsPerTable(tableNames){
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