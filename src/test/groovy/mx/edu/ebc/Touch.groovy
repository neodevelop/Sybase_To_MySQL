package org.edu.ebc

import groovy.sql.*

class Touch extends GroovyTestCase{
	void testConnect(){
		def sql = Sql.newInstance(
			"jdbc:jtds:sybase://192.1.2.247:2007/alu",
			//"jdbc:sybase:Tds:192.1.2.247:2007?ServiceName=alu",
			"adm_alu",
			"t_14z127au",
			"net.sourceforge.jtds.jdbc.Driver"
			//"com.sybase.jdbc4.jdbc.SybDriver"
			)
		def query = "select * from actividad where actividad_clave in (4,5)"

		//File f = new File("pinches_caracteres.txt")

		sql.eachRow(query){
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