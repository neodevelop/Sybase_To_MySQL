package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool

class SybaseToMysqlAsyncTests extends GroovyTestCase {
  void setUp(){

  }
  void testCountAsync(){
    def tableNames
    DB.instance.withSybaseInstance { sql ->
      tableNames = new DBInfo().getTableNamesNoCurry(sql)
    }
    log.info "${tableNames}"
    withPool {
      DB.instance.withSybaseInstance { sql ->
        tableNames.eachParallel { tn ->
          def dbInfo = new DBInfo()
          def c = dbInfo.countRowsPerTable(tn)
          log.info "$tn tiene $c registros"
        }
      }
    }
  }
}
