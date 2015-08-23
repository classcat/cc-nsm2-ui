package com.classcat

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

import org.apache.spark._
import org.apache.spark.SparkContext._

import com.classcat.ccnsm2.MyConfig
import com.classcat.ccnsm2.GData

object Boot extends App {
    println("")
    println("\tClassCat(R) Network Security Manager version 2")
    println("\tCopyright 2015 ClassCat Co.,Ltd. All rights reserved.")
    println("")

    println ("Load NSM2 configuration properties")
    try {
        val prop = new java.util.Properties()
        prop.load(new java.io.FileInputStream("%s/etc/ccnsm2.properties".format(MyConfig.myhome)))

        val myip = prop.getProperty("myip")
        val port = prop.getProperty("port")
        val bro_logs = prop.getProperty("bro_logs")

        MyConfig.myip = myip
        MyConfig.port_n = port.toInt
        MyConfig.bro_logs = bro_logs

    } catch {
        case ex:Exception => {
            println("Unexpected Error >> %s".format(ex.toString))
            sys.exit (-1)
        }
    }

    try {
        val sconf = new SparkConf().setMaster("local").setAppName("cc-nsm")
        val sc = new SparkContext(sconf)

        GData.sc = sc
    } catch {
        case ex:Exception => {
            println("Unexpected Error >> %s".format(ex.toString))
            sys.exit (-1)
        }
    }

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[CCNSM2ServiceActor], "classcat-nsm2-service")

  implicit val timeout = Timeout(5.seconds)
  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = MyConfig.port_n)
  // IO(Http) ? Http.Bind(service, interface = "0.0.0.0", port = 9000)
  // IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)
}
