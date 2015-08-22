
package com.classcat.ccnsm2

import org.apache.spark.rdd.RDD

class ViewCurrConn (is_error : Boolean, msg_error : String,
                                        proto : String,
                                        rdd_incoming : RDD[Array[String]],
                                        rdd_outgoing : RDD[Array[String]],
                                        rdd_others : RDD[Array[String]]
                                    ) extends ViewBasic (is_error, msg_error)
{

    buffer += curr_dt_str

    buffer += "<br/>"
    buffer += "<h2>最新のネットワーク接続</h2>"
    buffer += "<br/>"

    if (is_error) {
        buffer += """<div style="color:red;">ERROR >> %s</div>""".format(msg_error)
    } else {
        _make_contents
    }

    private def _make_contents : Unit = {

    }

}
