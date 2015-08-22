
package com.classcat.ccnsm2

import org.apache.spark.rdd.RDD

import org.joda.time.Instant
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

import java.net.InetAddress

class ViewCurrConn (is_error : Boolean, msg_error : String,
                                        proto : String,
                                        rdd_incoming : RDD[Array[String]],
                                        rdd_outgoing : RDD[Array[String]],
                                        rdd_others : RDD[Array[String]],
                                        rdd_incoming_group_by_orig_h: RDD[(String, Int)],
                                        rdd_outgoing_group_by_resp_h : RDD[(String, Int)],
                                        rdd_incoming_group_by_resp_p : RDD[(String, Int)],
                                        rdd_outgoing_group_by_resp_p : RDD[(String, Int)]
                                    ) extends ViewBasic (is_error, msg_error)
{

    buffer += curr_dt_str

    buffer += "<br/>"
    buffer += "<h2>最新のネットワーク接続 (%s)</h2>".format(proto.toUpperCase)
    buffer += "<br/>"

    if (is_error) {
        buffer += """<div style="color:red;">ERROR >> %s</div>""".format(msg_error)
    } else {
        _make_contents
    }

    private def _make_contents : Unit = {
        buffer += """<table>"""
        buffer += """<tr><td width="50%" valign="top">"""

        buffer += incoming_latest

        buffer += "<br/>"

        buffer += outgoing_latest

        buffer += "<br/>"

        buffer += others_latest

        buffer += """<td width="50%" valign="top">"""

        buffer += incoming_group_by_orig_h

        buffer += "<br/>"

        buffer += outgoing_group_by_resp_h

        buffer += "<br/>"

        buffer += incoming_group_by_resp_p

        buffer += "<br/>"

        buffer += outgoing_group_by_resp_p

        buffer += """</table>"""
    }


    def incoming_latest : String = {
        var lbuffer : String = ""

        val rdd_with_index : RDD[(Array[String], Long)] = rdd_incoming.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><strong>最新の %s 接続 (incoming)</strong></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>タイムスタンプ<th>接続元<th>ポート<th>接続先<th>ポート<th>プロトコル</tr>"

        rdd_with_index.take(50).foreach {
            x => {
                val tokens = x._1
                val index = x._2

                val ts = tokens(0)
                val ts2 = ts.toDouble*1000L
                var i = new Instant(ts2.longValue)
                val dt = i.toDateTime()

                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + dt.toString("HH:mm:ss.SSS MM/dd")
                // lbuffer += "<td>" + tokens(1)
                lbuffer += "<td>" + tokens(2)
                lbuffer += """<td align="center">""" + tokens(3)
                lbuffer += "<td>" + tokens(4)
                lbuffer += """<td align="center">""" + tokens(5)
                lbuffer += """<td align="center">""" + tokens(6) // protocol
            }
        }

        lbuffer += "</table>\n"

        return lbuffer
    }


    def outgoing_latest : String = {
        var lbuffer : String = ""

        val rdd_with_index : RDD[(Array[String], Long)] = rdd_outgoing.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><strong>最新の %s 接続 (outgoing)</strong></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>タイムスタンプ<th>接続元<th>ポート<th>接続先<th>ポート<th>プロトコル</tr>"

        rdd_with_index.take(50).foreach {
            x => {
                val tokens = x._1
                val index = x._2

                val ts = tokens(0)
                val ts2 = ts.toDouble*1000L
                var i = new Instant(ts2.longValue)
                val dt = i.toDateTime()

                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + dt.toString("HH:mm:ss.SSS MM/dd")
                // lbuffer += "<td>" + tokens(1)
                lbuffer += "<td>" + tokens(2)
                lbuffer += """<td align="center">""" + tokens(3)
                lbuffer += "<td>" + tokens(4)
                lbuffer += """<td align="center">""" + tokens(5)
                lbuffer += """<td align="center">""" + tokens(6) // protocol
            }
        }

        lbuffer += "</table>\n"

        return lbuffer
    }

    def others_latest : String = {
        var lbuffer : String = ""

        val rdd_with_index : RDD[(Array[String], Long)] = rdd_others.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><strong>最新の %s 接続 (others)</strong></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>タイムスタンプ<th>接続元<th>ポート<th>接続先<th>ポート<th>プロトコル</tr>"

        rdd_with_index.take(50).foreach {
            x => {
                val tokens = x._1
                val index = x._2

                val ts = tokens(0)
                val ts2 = ts.toDouble*1000L
                var i = new Instant(ts2.longValue)
                val dt = i.toDateTime()

                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + dt.toString("HH:mm:ss.SSS MM/dd")
                // lbuffer += "<td>" + tokens(1)
                lbuffer += "<td>" + tokens(2)
                lbuffer += """<td align="center">""" + tokens(3)
                lbuffer += "<td>" + tokens(4)
                lbuffer += """<td align="center">""" + tokens(5)
                lbuffer += """<td align="center">""" + tokens(6) // protocol
            }
        }

        lbuffer += "</table>\n"

        return lbuffer
    }


    def incoming_group_by_orig_h : String = {
        var lbuffer : String = ""

        val rdd_with_index = rdd_incoming_group_by_orig_h.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><b>%s 接続 (Incoming) 接続元上位</b></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>接続元<th>ホスト名<th>総数"

        rdd_with_index.take(10).foreach(
            x =>
            {
                val tpl = x._1
                val index = x._2

                val ip = tpl._1
                val hostname = InetAddress.getByName(ip).getHostName();
                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + tpl._1
                lbuffer += "<td>" + hostname
                lbuffer += "<td>" + tpl._2
            }
        )

        lbuffer += "</table>"

        return lbuffer
    }


    def outgoing_group_by_resp_h : String = {
        var lbuffer : String = ""

        val rdd_with_index = rdd_outgoing_group_by_resp_h.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><b>%s 接続 (Outgoing) 接続先上位</b></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>接続先<th>ホスト名<th>総数"

        rdd_with_index.take(10).foreach(
            x =>
            {
                val tpl = x._1
                val index = x._2

                val ip = tpl._1
                val hostname = InetAddress.getByName(ip).getHostName();

                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + tpl._1
                lbuffer += "<td>" + hostname
                lbuffer += "<td>" + tpl._2
            }
        )

        lbuffer += "</table>"

        return lbuffer
    }


    def incoming_group_by_resp_p : String = {
        var lbuffer : String = ""

        val rdd_with_index = rdd_incoming_group_by_resp_p.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><b>%s 接続 (Incoming) 接続先ポート上位</b></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>ポート<th>総数"

        rdd_with_index.take(10).foreach(
            x =>
            {
                val tpl = x._1
                val index = x._2
                // val ip = tpl._1

                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + tpl._1
                lbuffer += "<td>" + tpl._2
            }
        )

        lbuffer += "</table>"

        return lbuffer
    }


    def outgoing_group_by_resp_p : String = {
        var lbuffer : String = ""

        val rdd_with_index = rdd_outgoing_group_by_resp_p.zipWithIndex

        lbuffer += "<table>"
        lbuffer += "<caption><b>%s 接続 (Outgoing) 接続先ポート上位</b></caption>".format(proto.toUpperCase)
        lbuffer += "<tr><th><th>ポート<th>総数"

        rdd_with_index.take(10).foreach(
            x =>
            {
                val tpl = x._1
                val index = x._2
                // val ip = tpl._1

                lbuffer += "<tr>"
                lbuffer += "<td>" + (index+1).toString
                lbuffer += "<td>" + tpl._1
                lbuffer += "<td>" + tpl._2
            }
        )

        lbuffer += "</table>"

        return lbuffer
    }



}
