package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

@Log
class TablaAMigrar{
  def tableName
  def columnNames = []
  
  def processMetaMySQL = { metaData ->
    columnNames = metaData.fields.collect{ column ->
      column.name
    }
  }
  
  TablaAMigrar(){
    log.info "Starting conection"
  }
  
  def migrate = { sqlOrigin,sqlDestiny ->
    log.info "Starting migration of table $tableName"
    obtainColumnNames(sqlOrigin)
    makingBatchOperations(sqlDestiny,obtainDataFromOrigin(sqlDestiny).collect { currentMap -> currentMap*.value })
  }

  def count = { sql ->
    (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
  }
  
  private def obtainColumnNames(sql){
    sql.eachRow(("SELECT * FROM " + tableName),processMetaMySQL){ }
  }
  
  private def obtainDataFromOrigin(sql){
    def data = []
    //log.info queryFull.toString()
    sql.eachRow(("SELECT "+columnNames.join(',')+" FROM alu.dbo."+tableName)){ row ->
      def dataMap = [:]
      columnNames.each{ name ->
        dataMap."$name" = row["$name"]
      }
      data << dataMap
    }
    data
  }
  
  private def makingBatchOperations(sql,dataValue){
    //log.info "$dataValue"
    def parameterCounter = columnNames.size()
    def numberOfParamaters = []
    parameterCounter.times { numberOfParamaters << '?' }
    def insertSql = "INSERT INTO " + tableName + "("+columnNames.join(',')+") values (" + numberOfParamaters.join(',') +");"
    //log.info insertSql
    def updateCounts = sql.withBatch(insertSql) { ps ->
      dataValue.each { d ->
        ps.addBatch(d)
      }
    }
    updateCounts
  }
  
}