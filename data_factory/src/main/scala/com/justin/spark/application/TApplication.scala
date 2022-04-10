package com.justin.spark.application

import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

trait TApplication{
  var sc: SparkContext
  var spark: SparkSession



  def execute()( op: =>Unit ): Unit = {
    try {
      op
    } catch {
      case e: Exception => e.printStackTrace()
    }

  }

}
