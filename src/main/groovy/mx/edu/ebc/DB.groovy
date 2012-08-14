package mx.edu.ebc

import groovy.sql.*
import com.mchange.v2.c3p0.ComboPooledDataSource

@Singleton
class DB{

  def sqlSybase
  def sqlMySQL

  private DB() {

    ComboPooledDataSource sybase = new ComboPooledDataSource(
      driverClass:DBParameters.SYBASE_PARAMS.driver,
      jdbcUrl:DBParameters.SYBASE_PARAMS.url,
      user:DBParameters.SYBASE_PARAMS.user,
      password:DBParameters.SYBASE_PARAMS.password,
      minPoolSize: 30,
      maxPoolSize: 80,
      acquireIncrement: 10,
      numHelperThreads:40
    );

    sqlSybase = Sql.newInstance(sybase)

    ComboPooledDataSource mysql = new ComboPooledDataSource(
      driverClass:DBParameters.MYSQL_PARAMS.driver,
      jdbcUrl:DBParameters.MYSQL_PARAMS.url,
      user:DBParameters.MYSQL_PARAMS.user,
      password:DBParameters.MYSQL_PARAMS.password,
      minPoolSize: 30,
      maxPoolSize: 80,
      acquireIncrement: 10,
      numHelperThreads:40
    );
    sqlMySQL = Sql.newInstance(mysql)
  }

  def withSybaseInstance(closure){
    closure(sqlSybase)
  }

  def withMySQLInstance(closure){
    closure(sqlMySQL)
  }

}