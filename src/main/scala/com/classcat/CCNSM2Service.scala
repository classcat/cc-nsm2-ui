package com.classcat

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

import org.apache.spark._
import org.apache.spark.SparkContext._

import com.classcat.ccnsm2._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class CCNSM2ServiceActor extends Actor with CCNSM2Service {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(defaultRoute)
}


// this trait defines our service behavior independently from the service actor
trait CCNSM2Service extends HttpService {
    val prop = new java.util.Properties()
    try {
        prop.load(new java.io.FileInputStream("/usr/local/ccnsm2/etc/ccnsm2.properties"))
        val myip = prop.getProperty("myip")
        println(myip)
    } catch {
        case e:Exception => {
            println(e.toString)
        }
    }

    val conf = new SparkConf().setMaster("local").setAppName("cc-nsm")
    val sc = new SparkContext(conf)

    val defaultRoute =
        pathSingleSlash {
            redirect("/curr_conn?proto=tcp", StatusCodes.Found)
        } ~
        pathPrefix("ccimg") {
            getFromResourceDirectory("ccimg")
        }~
        pathPrefix("css") {
            getFromResourceDirectory("css")
        }~
        pathPrefix("img") {
            getFromResourceDirectory("img")
        }~
        pathPrefix("js") {
            getFromResourceDirectory("js")
        }~
        (path("curr_conn") & get) {
             parameter("proto")  { proto =>
                respondWithMediaType(`text/html`) {
                    complete {
                        proto match {
                            case "tcp" => {
                                val dc = new DataCurrConnTcp(sc)
                                println("HeyHey")
                            }
                            case _ => {
                                println("default")
                            }
                        }
                        val meta_refresh = """<meta http-equiv="refresh" content="90" />"""
                        html.view.render(meta_refresh, buffer).body
                    }
                }
             }
        }~
        path("xcurr_conn") {
            get {
                respondWithMediaType(`text/html`) {
                    complete {
                        <html>
                        <body>
                        にゃおちゃん隊ですにゃん。
                        </body>
                        </html>
                    }
                }
            }
        }~
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }
}
