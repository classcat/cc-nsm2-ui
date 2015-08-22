
package com.classcat.ccnsm2

import org.apache.spark.SparkContext
// import org.apache.spark._
import org.apache.spark.rdd.RDD // MapPartitionsRDD

import java.io.{PrintWriter, StringWriter}

// import com.classcat.ccnsm2.CCConfig

class DataCurrConn (proto : String) extends DataBasic { // sc : SparkContext) {

    private var rdd_incoming : RDD[Array[String]] = _
    private var rdd_outgoing : RDD[Array[String]] = _
    private var rdd_others : RDD[Array[String]] = _

    try {
        val log_file : String = "file://%s/current/conn.log".format(bro_logs)

        val rdd_raw : RDD[String] = sc.textFile(log_file).cache()

        println ("cc-info >> log file %s loaded".format(log_file))

        val rdd_all : RDD[Array[String]] = rdd_raw.filter(! _.startsWith("#")).map(_.split("\t"))

        println("cc-info >> tsv processed to get tokens.")

        // protocol specific rdd, sorted by ts
        /*
        #  fields ts(0)     uid(1)     id.orig_h(2)       id.orig_p(3)       id.resp_h(4)      id.resp_p(5)      proto(6)   service duration        orig_bytes      resp_bytes      conn_state      local_orig      local_resp      missed_bytes    history orig_pkts       orig_ip_bytes   resp_pkts       resp_ip_bytes   tunnel_parents
        # types  time    string  addr    port    addr    port    enum    string  interval        count   count   string  bool    bool    count   string  count   count   count   count   set[string]
        */

        var rdd : RDD[Array[String]] = null
        proto match {
            case "tcp" => { rdd = rdd_all.filter(_(6) == "tcp").sortBy( { x => x(0) }, false) }
        }

        println("cc-info >> filter applied for protocol specific")

        rdd_incoming = rdd.filter( { x => x(4) == MyConfig.myip } )
        rdd_outgoing = rdd.filter( { x =>  x(2) == MyConfig.myip } )
        rdd_others = rdd.filter( { x => (x(2) != MyConfig.myip) && (x(4) != MyConfig.myip) } )

    } catch {
        case ex : Exception  => {
            is_error = true
            msg_error = ex.toString

            println ("Unexpected Error >> %s".format(ex.toString))

            val sw = new StringWriter
            ex.printStackTrace(new PrintWriter(sw))
            println(sw.toString)
        }
    }

    def getRddIncoming () : RDD[Array[String]] = {
        return rdd_incoming
    }

    def getRddOutgoing () : RDD[Array[String]] = {
        return rdd_outgoing
    }

    def getRddOthers () : RDD[Array[String]] = {
        return rdd_others
    }

}
