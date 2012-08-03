package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class SybaseToMySQLOneTableTests extends GroovyTestCase{

  static sqlSybase
  static sqlMySQL
  static tableName = "acceso_motivo"
  static columnNames = []
  static processMeta = { metaData ->
    log.info "${metaData.properties}"
    log.info "${metaData.dump()}"
    columnNames = metaData.columns.collect{ column ->
      column.name
    }
  }

  void setUp(){
    sqlSybase = Sql.newInstance(
        DBParameters.SYBASE_PARAMS.url,
        DBParameters.SYBASE_PARAMS.user,
        DBParameters.SYBASE_PARAMS.password,
        DBParameters.SYBASE_PARAMS.driver
      )
    sqlMySQL = Sql.newInstance(
        DBParameters.MYSQL_PARAMS.url,
        DBParameters.MYSQL_PARAMS.user,
        DBParameters.MYSQL_PARAMS.password,
        DBParameters.MYSQL_PARAMS.driver
      )
  }

  void testConnected(){
    def query = "SELECT * FROM " + tableName
    def data = []
    sqlSybase.eachRow(query,processMeta){ row ->
      def dataMap = [:]
      columnNames.each{ name ->
        dataMap."$name" = row["$name"] // Dinamismo en mapas
      }
      data << dataMap
    }

    def dataValue = []
    data.each { currentMap ->
      dataValue << currentMap*.value
    }

    //log.info "$dataValue"
    def parameterCounter = columnNames.size()
    def numberOfParamaters = []
    parameterCounter.times { numberOfParamaters << '?' }
    def insertSql = "INSERT INTO " + tableName + "("+columnNames.join(',')+") values (" + numberOfParamaters.join(',') +");"
    //log.info insertSql
    def updateCounts = sqlMySQL.withBatch(insertSql) { ps ->
      dataValue.each { d ->
        ps.addBatch(d)
      }
    }
  }
}