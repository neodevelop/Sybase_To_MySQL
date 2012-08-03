package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class SybaseToMySQLOneTableTests extends GroovyTestCase{
  
  def table

  void setUp(){
    table = new TablaAMigrar()
    table.tableName = "actividades"
  }

  void testMigrate(){
    def updates = table.migrate()
    log.info "$updates"
  }
}