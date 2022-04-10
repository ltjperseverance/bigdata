package com.justin.spark.common

import com.justin.spark.util.FileUtil

import scala.collection.mutable
import scala.collection.mutable.HashMap

object DefaultProperties extends Serializable{
  private val defaultProperties = new HashMap[String, String]()
  private val filePath = "D:\\workspace\\idea\\bigdata\\data_factory\\src\\main\\resources\\application.properties"

  def getDefaultProperties(filePath: String=DefaultProperties.filePath): mutable.HashMap[String,String] ={
    if(defaultProperties.isEmpty){
      FileUtil.getPropertiesFromFile(filePath).foreach { case (k, v) =>
      defaultProperties(k) = v
      }
      defaultProperties
    }else{
      defaultProperties
    }
  }
}
