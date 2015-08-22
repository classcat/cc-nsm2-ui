
package com.classcat.ccnsm2

import org.joda.time.Instant
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

class ViewBasic (is_error : Boolean, msg_error : String) {
    protected var buffer : String = ""

    val curr_dt = DateTime.now()
    val curr_dt_str = "<div><b>現在時刻</b> : %s</div>".format(curr_dt.toString("YYYY/MM/dd HH:mm:ss"))

    def getHtml : String = {
        return buffer
    }

}
