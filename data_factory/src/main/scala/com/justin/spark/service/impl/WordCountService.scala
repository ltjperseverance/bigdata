package com.justin.spark.service.impl

import com.justin.spark.data.source.SeqSource
import com.justin.spark.service.TService
import org.apache.spark.sql.{DataFrame, SparkSession}

class WordCountService(spark: SparkSession) extends TService{
  private val source: SeqSource = new SeqSource
  override def run(): DataFrame = {
    val sourceDF: DataFrame = source.read(spark)
    sourceDF
  }
}

