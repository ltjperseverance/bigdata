package com.justin.spark.data.source

import org.apache.spark.sql.{DataFrame, SparkSession}

class SeqSource extends TSource {
  override def read(spark: SparkSession): DataFrame = {
    spark.createDataFrame(Seq(("zs", 1), ("ls", 3)))
  }
}
