package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool

class ReadingBigTablesFromFileAndMigrate extends GroovyTestCase {
  def migrateInfo

  void setUp(){
    migrateInfo = new MigrateInfo()
  }

  void testGettingTables(){
    assertNotNull migrateInfo
    assertNotNull migrateInfo.obtainTablesNamesFromFile()
    assertTrue migrateInfo.obtainTablesNamesFromFile().size() > 0
  }

  void testGettingTablesAndMigrate(){
    def intervals = []

    migrateInfo.obtainTablesNamesFromFile().each { name ->
      def table = new TablaAMigrar()
      table.tableName = name
      DB.instance.withMySQLInstance { sql ->
            table.obtainColumnNames(sql)
        }
      def intervalsT = table.generateIntervals()
      intervals.addAll(intervalsT)
    }

     migrateInfo.createPartialResultFile()
      withPool(50) {
          intervals.eachParallel { interval ->
              log.info "Migrando el intervalo $interval.offset con el maximo $interval.maximo "
              def result=interval.tablaAMigrar.migratePartialTable(interval.offset,interval.maximo)
              log.info "Informacion del resultado parcial $result"
              migrateInfo.savePartialResultToFile(result+"\n")
          }
      }
     migrateInfo.obtainTablesNamesFromFile().each {
      def tableT = new TablaAMigrar()
      tableT.tableName = name
      def dataCount = tableT.getCountTable()
      migrateInfo.saveResultToFile(dataCount+"\n")
    }
  }
}
