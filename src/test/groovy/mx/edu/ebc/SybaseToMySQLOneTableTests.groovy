package org.edu.ebc

import static groovyx.gpars.GParsPool.withPool
import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class SybaseToMySQLOneTableTests extends GroovyTestCase{
  
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

  void testFoo(){
    assert true
  }

  void _testMigrate(){
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