package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class DBInfo {

  def sqlSybase

  DBInfo(){
    sqlSybase = Sql.newInstance(
      DBParameters.SYBASE_PARAMS.url,
      DBParameters.SYBASE_PARAMS.user,
      DBParameters.SYBASE_PARAMS.password,
      DBParameters.SYBASE_PARAMS.driver
    )
  }

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
}