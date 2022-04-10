package com.justin.spark.data.source

import org.apache.spark.sql.SparkSession

trait TSource {
  def read(spark: SparkSession): Any
}
