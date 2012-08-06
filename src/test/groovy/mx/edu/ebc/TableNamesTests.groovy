package org.edu.ebc

import groovy.sql.*
import groovy.util.logging.*
import java.sql.*

class TableNamesTests extends GroovyTestCase {

  def dbInfo

  void setUp(){
    dbInfo = new DBInfo()
  }

  void testGetTableNames(){
    assert dbInfo.tableNames
  }

}