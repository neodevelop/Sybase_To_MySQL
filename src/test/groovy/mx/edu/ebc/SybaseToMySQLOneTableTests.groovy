package org.edu.ebc

import static groovyx.gpars.GParsPool.withPool
import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class SybaseToMySQLOneTableTests extends groovy.util.GroovyTestCase{
  
  def table1

  void setUp(){
    table1 = new TablaAMigrar()
    table1.tableName = "acceso_gafete"
  }

  void testMigrate(){
    table1.migrate()
  }
}