package org.edu.ebc

import groovy.sql.*

class PingTests extends GroovyTestCase{

	def sqlSybase

	void setUp(){
		sqlSybase = Sql.newInstance(
      DBParameters.SYBASE_PARAMS.url,
      DBParameters.SYBASE_PARAMS.user,
      DBParameters.SYBASE_PARAMS.password,
      DBParameters.SYBASE_PARAMS.driver
    )
	}

	void testConnect(){
		def query = "select 2+2 as SUMA"
		sqlSybase.eachRow(query){ row ->
			log.info "${row}"
		}
	}
}