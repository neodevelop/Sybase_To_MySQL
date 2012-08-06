package mx.edu.ebc

import groovy.sql.*

class DB{

  static sqlSybase;
  static sqlMySQL;

  static {
    sqlSybase = Sql.newInstance(
      DBParameters.SYBASE_PARAMS.url,
      DBParameters.SYBASE_PARAMS.user,
      DBParameters.SYBASE_PARAMS.password,
      DBParameters.SYBASE_PARAMS.driver
    )
    sqlMySQL = Sql.newInstance(
      DBParameters.MYSQL_PARAMS.url,
      DBParameters.MYSQL_PARAMS.user,
      DBParameters.MYSQL_PARAMS.password,
      DBParameters.MYSQL_PARAMS.driver
    )
  }

  static withSybaseInstance(closure){
    closure(sqlSybase)
  }

  static withMySQLInstance(closure){
    closure(sqlMySQL)
  }

  
}