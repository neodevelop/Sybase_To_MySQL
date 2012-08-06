package org.edu.ebc

import groovy.sql.*

class DB{

  static sqlSybase;

  static {
    sqlSybase = Sql.newInstance(
      DBParameters.SYBASE_PARAMS.url,
      DBParameters.SYBASE_PARAMS.user,
      DBParameters.SYBASE_PARAMS.password,
      DBParameters.SYBASE_PARAMS.driver
    )
  }

  static withSybaseInstance(closure){
    closure(sqlSybase)
  }

  
}