package com.justin.spark

import com.justin.spark.application.WordCountApplication
import com.justin.spark.common.DefaultProperties
import com.justin.spark.constants.Constants
import com.justin.spark.util.FileUtil
import org.apache.spark.util.Utils
import org.slf4j.LoggerFactory

import java.lang.reflect.Constructor
import scala.collection.mutable.HashMap

object Main {
  private val log = LoggerFactory.getLogger("Main")
  def main(args: Array[String]): Unit = {
    val application = "WordCountApplication"
    val name = "WordCount"
    val mode = "local"
    val defaultProperties = DefaultProperties.getDefaultProperties()
    defaultProperties.foreachEntry(
      (k,v) => {
        println(s"$k=$v")
      }
    )

    Constants.MAP_APPLICATION.keys.foreach(
      k => k match {
        case application =>
          val reference:String = Constants.MAP_APPLICATION.get(application).get
          val clazz: Class[_] = Class.forName(reference)
          val constructor: Constructor[_] = clazz.getConstructor(classOf[String], classOf[String])
          constructor.newInstance(name,mode).asInstanceOf[WordCountApplication]

        case _ => log.error("app error")

      }
    )


  }

}
