package com.justin.core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object CreateRDD {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("WordCount")
    val sc: SparkContext = new SparkContext(conf)

    // 方式一： 从集合创建RDD
    val rdd: RDD[Int] = sc.parallelize(Seq(1, 2, 3, 4, 5))

    // makeRDD 见名知意   底层调parallelize
    val rdd1: RDD[User] = sc.makeRDD(Seq(User("zs", 20), User("ls", 19), User("wf", 18)))

    // saveAsTextFile可以生成分区文件
    rdd1.saveAsTextFile("output")
  }

}
case class User(name: String, age: Int)
