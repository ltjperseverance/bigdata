
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, LongType, StringType, StructField, StructType}

object Test {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .master("local[2]")
      .appName("test")
//      .enableHiveSupport()
      .getOrCreate()
//    import spark.implicits._

    val eventSchema = new StructType(Array(
      StructField("time", LongType, false),
      StructField("event", StringType, false),
      StructField("login_id", StringType, false),
      StructField("used_time_clean", IntegerType, false),
      StructField("datetime", StringType, false)
    ))

    val eventRows = Array(Row(1640966410L, "out","1",10,"2022/1/1 0:00:10"),Row(1640966430L, "out","1",11,"2022/1/1 0:00:10"),
      Row(1640966460L, "finish","1",20,"2022/1/1 0:00:10"),Row(1640966465L, "out","1",2,"2022/1/1 0:00:10"),
      Row(1641052860L, "finish","1",20,"2022/1/1 0:00:10"),Row(1641052865L, "out","1",2,"2022/1/1 0:00:10"),
      Row(1641053100L, "finish","1",20,"2022/1/1 0:00:10"),Row(1641139260L, "finish","1",20,"2022/1/1 0:00:10"),
      Row(1641139320L, "out","1",20,"2022/1/1 0:00:10"))
    val eventRDD = spark.sparkContext.makeRDD(eventRows)
    val eventDf = spark.createDataFrame(eventRDD, eventSchema)

    eventDf.show()
    eventDf.createOrReplaceTempView("event_table");

    // create user data
    val userSchema = new StructType(Array(
      StructField("user_id", StringType, false),
      StructField("baby_birthday", StringType, false),
      StructField("city", StringType, false),
      StructField("city_level", StringType, false)
    ))

    val userRows = Seq(Row("1","2021/1/1","成都","一线"))
    val userRDD = spark.sparkContext.parallelize(userRows)
    val userDf = spark.createDataFrame(userRDD, userSchema)
    //    userDf.show()
    userDf.createOrReplaceTempView("user")

    // handle
    val tmp1 = spark.sql(
      """
        |select *
        |from (
        | select time,event,login_id,used_time_clean ,
        |   Row_Number() OVER (PARTITION BY event ORDER BY time ) id,
        |   Row_Number() OVER (ORDER BY time ) rank
        |   from event_table order by login_id,time ) tmp
        |where tmp.event = 'finish'
        |""".stripMargin)

    tmp1.show()
    tmp1.createOrReplaceTempView("tmp2")

    val tmp3 =  spark.sql(
      """
        | select time as endtime,event,login_id,used_time_clean,id,rank,
        |  lead(rank,1,-1) over (partition by event order by event ) as lead_rank,
        |  lag(time,1,1) over (partition by event order by event ) as start_time
        |  from tmp2
        |""".stripMargin)
    tmp3.show()
    tmp3.createOrReplaceTempView("tmp3")

    val tmp4 = spark.sql(
      """
        | select
        |     id,tmp3.event as f_event,event_table.event as org_event, tmp3.login_id,start_time,endtime,( cast(lead_rank as int) - cast(rank as int)) as ct,event_table.used_time_clean
        |   from event_table right join tmp3
        |   on
        |   event_table.login_id = tmp3.login_id and event_table.time < tmp3.endtime and event_table.time > tmp3.start_time
        |""".stripMargin)

    tmp4.show(200)
    tmp4.createOrReplaceTempView("tmp4")

    // 对null值，或者负值进行处理
    val tmp5 = spark.sql(
      """
        | select
        |   id,f_event,org_event,login_id,start_time,endtime,if(ct < 0,0,ct) as ct,if(used_time_clean is null,0,used_time_clean) as used_time_clean
        |   from tmp4
        |""".stripMargin)
    tmp5.show()
    tmp5.createOrReplaceTempView("tmp5")

    // 进行聚合处理，计算时间只和
    val tmp6 = spark.sql(
      """
        | select id,f_event,sum(used_time_clean) as ct_time
        | from tmp5 group by f_event,id order by id
        |""".stripMargin)
    tmp6.show()
  }
}
