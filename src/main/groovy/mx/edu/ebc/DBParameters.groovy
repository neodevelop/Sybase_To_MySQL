package mx.edu.ebc

interface DBParameters{
  def SYBASE_PARAMS = [
    //url:"jdbc:jtds:sybase://192.1.2.247:2007/alu",
    url:"jdbc:datadirect:sybase://192.1.2.247:2007;DatabaseName=alu",
    user:"adm_alu",
    password:"t_14z127au",
    //driver:"net.sourceforge.jtds.jdbc.Driver"
    driver:"com.ddtek.jdbc.sybase.SybaseDriver"
  ]
  def MYSQL_PARAMS = [
    url:"jdbc:mysql://192.1.2.229/isybase",
    user:"sybase",
    password:"s1b4s3",
    driver:"com.mysql.jdbc.Driver"
  ]

  def SYBASE_SELECT_MAX_ROWS=4000

}