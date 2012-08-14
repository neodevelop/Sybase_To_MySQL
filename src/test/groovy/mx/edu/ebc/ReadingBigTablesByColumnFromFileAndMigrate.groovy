package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool

class ReadingBigTablesByColumnFromFileAndMigrate extends GroovyTestCase {


  void testGettingTablesAndMigrate(){
    def intervals = []
      def table = new TablaAMigrar()
      table.tableName = "his_calif_faltas"
      def intervalsT = table.generateIntervalsByColumn("himater_deta_folio")

      withPool(30) {
          intervalsT.eachParallel { interval ->
              log.info "Migrando el intervalo $interval.offset con el maximo $interval.maximo "
              def result=interval.tablaAMigrar.migratePartialTableByColumn(interval.offset,interval.maximo,interval.columnName)
              log.info "Informacion del resultado parcial $result"
          }
      }
  }
}
