package mx.edu.ebc

class PingTests extends groovy.util.GroovyTestCase{

	void setUp(){
		
	}

	void testConnect(){
		def query = "select 2+2 as SUMA"
		DB.instance.withSybaseInstance { sql ->
      assert sql.firstRow(query)[0] == 4
    }
    DB.instance.withMySQLInstance { sql ->
      assert sql.firstRow(query)[0] == 4
    }
	}
}