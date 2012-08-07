package mx.edu.ebc

import static groovyx.gpars.GParsPool.withPool
import groovy.sql.DataSet

class ReadingTablesFromFileAndMigrate extends GroovyTestCase {
  def migrateInfo

  void setUp(){
    migrateInfo = new MigrateInfo()
  }

  void testGettingTables(){
    assertNotNull migrateInfo
    assertNotNull migrateInfo.obtainTablesNamesFromFile()
    assertTrue migrateInfo.obtainTablesNamesFromFile().size() > 0
  }

  void testGettingTablesAndMigrate(){
    def tablesToMigrate = []
    migrateInfo.obtainTablesNamesFromFile().each { name ->
      def table = new TablaAMigrar()
      table.tableName = name
      tablesToMigrate << table
    }
    assertEquals tablesToMigrate.size(), migrateInfo.obtainTablesNamesFromFile().size()
    withPool {
      tablesToMigrate.eachParallel { tableToMigrate ->
        log.info "Migrando la tabla $tableToMigrate.tableName"
        tableToMigrate.migrate()
        log.info "Tabla $tableToMigrate.tableName migrada"
      }
    }
  }
}
