package mx.edu.ebc

/**
 * Esta clase prueba como obtener la primaries key
 * User: jkings
 * Date: 7/08/12
 * Time: 03:12 PM
 * To change this template use File | Settings | File Templates.
 */
class ColumnsNamesTest extends GroovyTestCase {


    def table1

    void setUp() {
        table1 = new TablaAMigrar()
        table1.tableName = "persons_disciplina"

    }


  void testGetColumnsName() {

      DB.instance.withMySQLInstance{ sql ->
          def listaColumnas = table1.obtainColumnNames(sql)

          assert listaColumnas

          listaColumnas.each { columna ->

              println  columna

          }
      }


  }

}
