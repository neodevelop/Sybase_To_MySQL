package mx.edu.ebc

class TableNamesTests extends groovy.util.GroovyTestCase {

  def dbInfo

  void setUp(){
    dbInfo = new DBInfo()
  }

  void testGetTableNames(){
    assert dbInfo.tableNames
  }

}