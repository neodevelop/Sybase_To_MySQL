package mx.edu.ebc

/**
 * Esta clase prueba como obtener la primaries key
 * User: jkings
 * Date: 7/08/12
 * Time: 03:12 PM
 * To change this template use File | Settings | File Templates.
 */
class PrimariesKeysTest extends GroovyTestCase {


    def dbInfo

    void setUp() {

      dbInfo = new DBInfo()

    }


  void testGetPrimaryKey() {

      DB.instance.withMySQLInstance{ sql ->

          def listaColumnas = dbInfo.getPrimaryKey(sql, "Cat_Beca")

          assert  listaColumnas
      }


  }

}
