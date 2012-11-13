package mx.edu.ebc

class RowsCountTest extends groovy.util.GroovyTestCase{
    MigrateInfo migrateInfo
	void setUp(){
        migrateInfo = new MigrateInfo()
	}

	void testRowCount(){
        migrateInfo.createNewResultFile()
        migrateInfo.obtainTablesNamesAndColumnFromFile().each {  line ->
            def campos= line.tokenize("|")
            def tableT = new TablaAMigrar()
            tableT.tableName = campos[0]
            def dataCount = tableT.getCountTable()
            migrateInfo.saveResultToFile(dataCount+"\n")
        }

	}
}