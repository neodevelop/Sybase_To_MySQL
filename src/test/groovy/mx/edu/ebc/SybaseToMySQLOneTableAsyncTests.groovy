package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool

class SybaseToMySQLOneTableAsyncTests extends groovy.util.GroovyTestCase{
  
  def tables

  void setUp(){
    def table1 = new TablaAMigrar()
    table1.tableName = "actividades"
    def table2 = new TablaAMigrar()
    table2.tableName = "acceso_gafete"
    def table3 = new TablaAMigrar()
    table3.tableName = "bajas_acad_periodos"
    tables = [table1,table2,table3]
  }

  void testMigrate(){
    log.info "Iniciando migración"
    withPool{
      log.info "With pool start"
      tables.eachParallel{ t ->
        def u = t.migrate()
        log.info "$u"
      }
      log.info "With pool end"
    }
    log.info "Terminando migración"
  }
}