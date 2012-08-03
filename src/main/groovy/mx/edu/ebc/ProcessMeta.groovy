package org.edu.ebc

class ProcessMeta {
  
  def processMetaGeneric = { metaData ->
    columnNames = metaData.columns.collect{ column ->
      column.name
    }
  }
  
  def processMetaMySQL = { metaData ->
    //log.info "${metaData}"
    //log.info "${metaData.properties}"
    //log.info "${metaData.dump()}"
    return metaData.fields.collect{ column ->
      column.name
    }
  }

  def processMetaSybase = { metaData ->
    //log.info "${metaData.metaClass}"
    //log.info "${metaData.properties}"
    //log.info "${metaData.dump()}"
    def methods = ('a'..'z')
    methods.each { l->
      def method = metaData.metaClass.respondsTo(metaData,"get${l}")
      def property = metaData.metaClass.hasProperty(metaData,"${l}")
      //log.info "MetaData has property ${l} = ${property}"
      //log.info "MetaData has property ${l} = ${property?.dump()}"
      //log.info "MetaData respondsTo method ${l} = ${method}"
      //log.info "MetaData has method ${l} = ${method?.dump()}"
    }
  }
}