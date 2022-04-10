import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(conf)
    val lines: RDD[String] = sc.textFile("spark_sql/src/main/resources/wc.txt")
    val wordCount: RDD[(String, Int)] = lines
      .flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .sortByKey()
    wordCount.collect().foreach(println)
  }

}
