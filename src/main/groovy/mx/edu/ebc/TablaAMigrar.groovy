package mx.edu.ebc

import groovy.util.logging.*

@Log
class TablaAMigrar{
  private def tableName
  def columnNames = []
  
  def processMetaMySQL = { metaData ->
    columnNames = metaData.fields.collect{ column ->
      column.name
    }
  }
  
  TablaAMigrar(){
    
  }

  void setTableName(def tableName) {
      this.tableName = tableName
      DB.instance.withMySQLInstance { sql ->
          obtainColumnNames(sql)
      }
  }

  def migrate() {
      def numRows
      DB.instance.withSybaseInstance() { sql ->
          numRows = count(sql)
      }
      if (numRows <= DBParameters.SYBASE_SELECT_MAX_ROWS) {
          migrateSmallTable(numRows)
      } else {
          migrateBigTable(numRows)
      }
  }

  def count = { sql ->
    (sql.firstRow("SELECT COUNT(*) AS counter FROM " + tableName))["counter"]
  }

  def delete = { sql, tableName ->
   sql.executeUpdate("delete from isybase.$tableName")
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
        if (row["$name"]!= null)
        log.info row["$name"].class.name
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

  private def migrateSmallTable(def numRows) {

      def data
      def result
      def exception =""
      def numRowsMySQL
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
              delete(sql,tableName)
          }
      }
      def tiempoFinal=new Date()
      DB.instance.withMySQLInstance() { sql ->
          numRowsMySQL = count(sql)
      }
      return "$tableName|$numRows|$numRowsMySQL|"+(numRows==numRowsMySQL) +"|" +( (tiempoFinal.time - tiempoInicial.time)/60000 )+"|$exception"
  }


  private def migrateBigTable(def numRows) {
      def data
      def result=0
      def sqlMySql =   DB.instance.sqlMySQL
      def sqlSybase = DB.instance.sqlSybase
      def exception=""
      int offset = 1
      int maximo=DBParameters.SYBASE_SELECT_MAX_ROWS
      def temp = maximo
      def numRowsMySQL
      log.info "El numero de renglones para la tabla $tableName  es : $numRows"
      def tiempoInicial = new Date()
      while (true) {
          if (offset + maximo > numRows) {
                temp = numRows - offset +1
           }
          try {
              log.info "Obteniendo la informacion de la tabla $tableName"
              data = obtainDataFromOrigin(sqlSybase,offset,temp).collect { currentMap -> currentMap*.value }
              log.info "Persistiendo la informacion de la tabla $tableName en MYSQL"
              result = makingBatchOperations(sqlMySql,data)
              log.info "Informacion  persistida en la tabla $tableName de mysql"
          }catch(Throwable e){
              exception= e.message
              log.info "***Error insertando datos en $tableName ***"
              delete(sqlMySql,tableName)
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

  private def migratePartialTable(def offset, def maximo) {
        def data
        def result=0
        def sqlMySql =   DB.instance.sqlMySQL
        def sqlSybase = DB.instance.sqlSybase
        def exception=""
        def numRowsMySQL
        log.info "El numero de renglones para migrar de la tabla $tableName  es : $maximo"
        def tiempoInicial = new Date()
        try {
            log.info "Obteniendo la informacion de la tabla $tableName"
            data = obtainDataFromOrigin(sqlSybase,offset,maximo).collect { currentMap -> currentMap*.value }
            log.info "Persistiendo la informacion de la tabla $tableName en MYSQL"
            result = makingBatchOperations(sqlMySql,data)
            log.info "Informacion  persistida en la tabla $tableName de mysql"
        }catch(Throwable e){
            exception= e.message
            log.info "***Error insertando datos en $tableName ***"
            delete(sqlMySql,tableName)
        }
        def tiempoFinal=new Date()
        log.info "Proceso de migracion parcial de tabla terminado"
        return "$tableName|$maximo|" +((tiempoFinal.time - tiempoInicial.time)/60000 )+"|$exception"
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

   def generateIntervals() {
        int offset = 1
        int maximo=DBParameters.SYBASE_SELECT_MAX_ROWS
        def numRows
        def temp = maximo
        DB.instance.withSybaseInstance() { sql ->
           numRows = count(sql)
        }
        def intervalList =[]
        while (true) {
            if (offset + maximo > numRows) {
                temp = numRows - offset +1
            }

        def interval = new Interval(offset: offset, maximo: temp,tablaAMigrar: this)
        intervalList.add(interval)
        offset += maximo
        if (offset > numRows)
            break

        }
       intervalList
    }

    def getCountTable() {

        def numRowsSybase
        DB.instance.withSybaseInstance() { sql ->
            numRowsSybase = count(sql)
        }
        def numRowsMySQL
        DB.instance.withMySQLInstance() { sql ->
            numRowsMySQL = count(sql)
        }

        return "$tableName|$numRowsSybase|$numRowsMySQL|"+(numRowsSybase==numRowsMySQL)
    }
}

