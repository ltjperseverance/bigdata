package com.justin.it.test


import redis.RedisClient

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
object TestRedis{

  def main(args: Array[String]): Unit = {
    implicit val akkaSystem = akka.actor.ActorSystem()
    val redis = RedisClient(host = "119.91.155.152",port = 6379,password = Some("Litingjian#2022"))
    redis.ping()
//    val futurePong = redis.ping()
//    println("Ping sent!")
//    futurePong.map(pong => {
//      println(s"Redis replied with a $pong")
//    })
//    Await.result(futurePong, 5 seconds)
//    val eventualMaybeString = redis.get("foo").onComplete{
//  case some: Some
  }
    print(eventualMaybeString)


  }
}
