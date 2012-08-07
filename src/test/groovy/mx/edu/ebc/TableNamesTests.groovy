package mx.edu.ebc

class TableNamesTests extends groovy.util.GroovyTestCase {

  def dbInfo

  void setUp(){
    dbInfo = new DBInfo()
  }

  void testGetTableNames(){
    DB.instance.withSybaseInstance { sql ->
      assert dbInfo.getTableNamesNoCurry(sql)
    }
  }

}