package mx.edu.ebc

import groovy.util.logging.*

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
    
  }
  
  def migrate() {

    def data
    def result

    DB.instance.withMySQLInstance { sql ->
      obtainColumnNames(sql)
    }

    DB.instance.withSybaseInstance() { sql ->
      data = obtainDataFromOrigin(sql).collect { currentMap -> currentMap*.value }
    }
    DB.instance.withMySQLInstance { sql ->
      result = makingBatchOperations(sql,data)
    }
    result
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

  def migrateBigTable() {

      def data
      def result

      def sqlMySql =   DB.instance.sqlMySQL
      def sqlSybase = DB.instance.sqlSybase

      obtainColumnNames(sqlMySql)


      int offset = 1
      int maximo=DBParameters.SYBASE_SELECT_MAX_ROWS
      boolean  continuaProceso = true
      def temp = maximo

      def numRows

      DB.instance.withSybaseInstance() { sql ->
          numRows = count(sql)
      }

      log.info "El numero de renglones para la tabla $tableName  es : $numRows"

      while (continuaProceso) {


      if (offset + maximo > numRows) {

            temp = numRows - offset +1

        }

      data = obtainDataFromOrigin(sqlSybase,offset,temp).collect { currentMap -> currentMap*.value }

      result = makingBatchOperations(sqlMySql,data)

       offset += maximo

       if (offset > numRows)
           break

      }

      log.info "Proceso de migracion de tabla terminado"

      result


  }


    private def obtainDataFromOrigin(sql, offset, limit){
        def data = []
        //log.info queryFull.toString()
        sql.eachRow(("SELECT "+columnNames.join(',')+" FROM alu.dbo."+tableName),offset,limit){ row ->
            def dataMap = [:]
            columnNames.each{ name ->
                dataMap."$name" = row["$name"]
            }
            data << dataMap
        }
        data
    }

}