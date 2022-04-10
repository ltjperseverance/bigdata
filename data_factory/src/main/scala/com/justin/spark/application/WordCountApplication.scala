package com.justin.spark.application
import com.justin.spark.service.impl.WordCountService
import org.apache.spark.SparkContext
import org.apache.spark.sql.{DataFrame, SparkSession}

class WordCountApplication(name:String,mode:String=null) extends TApplication {
  override var sc: SparkContext = _
  override var spark: SparkSession = _


  execute(){
    mode match {
      case "local" => spark = SparkSession.builder().appName(name).master("local[*]").getOrCreate()
      case "hive" => spark = SparkSession.builder().appName(name).enableHiveSupport().getOrCreate()
      case _ => spark = SparkSession.builder().appName(name).getOrCreate()
    }
    val task = new WordCountService(spark)
    val result: DataFrame = task.run()
    result.show()
    spark.stop()
  }
}
