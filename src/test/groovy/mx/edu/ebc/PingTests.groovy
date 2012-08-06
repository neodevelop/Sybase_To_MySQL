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
		def query = "select * from actividad where actividad_clave in (4,5)"
		//File f = new File("pinches_caracteres.txt")
		sqlSybase.eachRow(query){
			//println it
			InputStreamReader ir = new InputStreamReader(it.actividad_descripcion.asciiStream)
			StringBuilder sb=new StringBuilder()
			BufferedReader br = new BufferedReader(ir)
			String read = br.readLine()
			while(read != null) {
			    //System.out.println(read);
			    sb.append(read)
			    read =br.readLine()
			}
			println sb.toString()
			//f << sb.toString()
		}
	}
}