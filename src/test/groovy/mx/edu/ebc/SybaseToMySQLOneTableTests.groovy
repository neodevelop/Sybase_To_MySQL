package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class SybaseToMySQLOneTableTests extends GroovyTestCase{

  static sqlSybase
  static sqlMySQL
  static tableName = "actividades"
  static columnNames = []

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
    
    def querySimple = "SELECT * FROM " + tableName
    sqlMySQL.eachRow(querySimple,columnNames = new ProcessMeta().processMetaMySQL){
      columnNames.each{ name ->
        dataMap."$name" = row["$name"] 
      }
    }
    def queryFull = "SELECT "+columnNames.join(',')+" FROM alu.dbo."+tableName
    log.info queryFull.toString()

    def data = []
    sqlSybase.eachRow(queryFull){ row ->
      def dataMap = [:]
      columnNames.each{ name ->
        dataMap."$name" = row["$name"]
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