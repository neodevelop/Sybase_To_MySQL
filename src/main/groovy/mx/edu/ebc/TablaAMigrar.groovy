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
    def exception =""
    def numRows
    def numRowsMySQL
    DB.instance.withMySQLInstance { sql ->
      obtainColumnNames(sql)
    }
     DB.instance.withSybaseInstance() { sql ->
       numRows = count(sql)
     }
     def tiempoInicial = new Date()
      DB.instance.withSybaseInstance() { sql ->
      data = obtainDataFromOrigin(sql).collect { currentMap -> currentMap*.value }
    }
    DB.instance.withMySQLInstance { sql ->
      try{
        result = makingBatchOperations(sql,data)
      }catch(Throwable e){
        log.info "***Error insertando datos en $tableName ***"
          exception= e.message
      }
    }
     def tiempoFinal=new Date()
      DB.instance.withMySQLInstance() { sql ->
          numRowsMySQL = count(sql)
      }
      return "$tableName|$numRows|$numRowsMySQL|"+(numRows==numRowsMySQL) +"|" +( (tiempoFinal.time - tiempoInicial.time)/60000 )+"|$exception"
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
    def temporal = columnNames.join(',')
    temporal = cleanReservadesWord(temporal)
    def insertSql = "INSERT INTO " + tableName + "("+temporal+") values (" + numberOfParamaters.join(',') +");"
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
      def result=0
      def sqlMySql =   DB.instance.sqlMySQL
      def sqlSybase = DB.instance.sqlSybase
      def exception=""
      obtainColumnNames(sqlMySql)
      int offset = 1
      int maximo=DBParameters.SYBASE_SELECT_MAX_ROWS
      def temp = maximo
      def numRows = count(sqlSybase)
      def numRowsMySQL
      log.info "El numero de renglones para la tabla $tableName  es : $numRows"
      def tiempoInicial = new Date()
      while (true) {
          if (offset + maximo > numRows) {
                temp = numRows - offset +1
           }
          try {
              data = obtainDataFromOrigin(sqlSybase,offset,temp).collect { currentMap -> currentMap*.value }
              result = makingBatchOperations(sqlMySql,data)
          }catch(Throwable e){
              log.info "***Error insertando datos en $tableName ***"
              exception= e.message
              break
          }
          offset += maximo
          if (offset > numRows)
               break
      }
      def tiempoFinal=new Date()
      log.info "Proceso de migracion de tabla terminado"
      numRowsMySQL = count(sqlMySql)

     return "$tableName|$numRows|$numRowsMySQL|"+(numRows==numRowsMySQL) +"|" +((tiempoFinal.time - tiempoInicial.time)/60000 )+"|$exception"

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

    private def cleanReservadesWord(word) {
        if (word.indexOf("long")>=0) {
            word =  word.replaceFirst("long","`long`")
        }
        return word
    }
}