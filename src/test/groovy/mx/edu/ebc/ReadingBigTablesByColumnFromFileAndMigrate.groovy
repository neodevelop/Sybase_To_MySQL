package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool

class ReadingBigTablesByColumnFromFileAndMigrate extends GroovyTestCase {

    MigrateInfo migrateInfo

    void setUp(){
        migrateInfo = new MigrateInfo()
    }

    void testGettingTables(){
        assertNotNull migrateInfo
        def lineas =migrateInfo.obtainTablesNamesAndColumnFromFile()
        assertNotNull lineas
        assertTrue lineas.size() > 0
  }

  void testGettingTablesAndMigrate(){
    def intervals = []
    def lineas =migrateInfo.obtainTablesNamesAndColumnFromFile()
    lineas.each { line ->
          def campos= line.tokenize("|")
          def tamanoMaximo = DBParameters.SYBASE_SELECT_MAX_ROWS
          if (campos.size() > 2)
               tamanoMaximo = Integer.parseInt(campos[2])
          log.info("Se migrara la tabla " + campos[0] +" con el campo "+ campos[1] +" y se traera fracciones de " +tamanoMaximo)
         def table = new TablaAMigrar()
         table.tableName = campos[0]
         def intervalsT = table.generateIntervalsByColumn(campos[1],tamanoMaximo)
         intervals.addAll(intervalsT)
     }
     withPool(30) {
      intervals.eachParallel { interval ->
        log.info "Migrando el intervalo $interval.offset a $interval.maximo "
        def result=interval.tablaAMigrar.migratePartialTableByColumn(interval.offset,interval.maximo,interval.columnName)
        log.info "Informacion del resultado parcial $result"
       }
     }
      migrateInfo.createNewResultFile()
      migrateInfo.obtainTablesNamesAndColumnFromFile().each {
          def tableT = new TablaAMigrar()
          tableT.tableName = name
          def dataCount = tableT.getCountTable()
          migrateInfo.saveResultToFile(dataCount+"\n")
      }
  }
}
