package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool

class SybaseToMySQLOneBIgTableTests extends groovy.util.GroovyTestCase{
  
    def  migrateInfo

  void setUp(){
      migrateInfo = new MigrateInfo()
  }

  void testMigrateBigTable(){
      def table1 = new TablaAMigrar()
      table1.tableName = "doctos_dia_calen"
      def intervals = table1.generateIntervals()
      withPool(50) {
          intervals.eachParallel { interval ->
              log.info "Migrando el intervalo $interval.offset con el maximo $interval.maximo "
              def result=interval.tablaAMigrar.migratePartialTable(interval.offset,interval.maximo)
              log.info "Informacion del resultado parcial $result"
              migrateInfo.saveResultToFile(result+"\n")
          }
      }
  }
}