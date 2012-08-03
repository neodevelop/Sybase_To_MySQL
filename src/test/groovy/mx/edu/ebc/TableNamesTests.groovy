package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class TableNamesTests extends GroovyTestCase {

  static sql

  void setUp(){
    log.info "Creando conexión a base de datos Sybaseº"
    sql = Sql.newInstance(
      "jdbc:jtds:sybase://192.1.2.247:2007/alu",
      "adm_alu",
      "t_14z127au",
      "net.sourceforge.jtds.jdbc.Driver"
    )
    log.info "Conexión creada!"
  }

  void testTablesNames(){
    log.info "Obteniendo datos de tablas"
    DatabaseMetaData dbm = sql.connection.metaData
    //log.info dbm.properties
    def types = ["TABLE"]
    ResultSet rs = dbm.getTables(null,null,"%",types as String[])
    while (rs.next()){
      String table = rs.getString("TABLE_NAME")
      println(table)
    }
  }

}