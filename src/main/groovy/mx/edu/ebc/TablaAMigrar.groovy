package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

@Log
class TablaAMigrar{
  def sqlSybase
  def sqlMySQL
  def tableName
  def columnNames = []
  
  def processMetaMySQL = { metaData ->
    columnNames = metaData.fields.collect{ column ->
      column.name
    }
  }
  
  TablaAMigrar(){
    log.info "Starting conection"
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
  
  def migrate(){
    log.info "Starting migration of table $tableName"
    obtainColumnNames()
    makingBatchOperations(obtainDataFromOrigin().collect { currentMap -> currentMap*.value })
  }
  
  private def obtainColumnNames(){
    sqlMySQL.eachRow(("SELECT * FROM " + tableName),processMetaMySQL){ }
  }
  
  private def obtainDataFromOrigin(){
    def data = []
    //log.info queryFull.toString()
    sqlSybase.eachRow(("SELECT "+columnNames.join(',')+" FROM alu.dbo."+tableName)){ row ->
      def dataMap = [:]
      columnNames.each{ name ->
        dataMap."$name" = row["$name"]
      }
      data << dataMap
    }
    data
  }
  
  private def makingBatchOperations(dataValue){
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
    updateCounts
  }
  
}