
package com.classcat.ccnsm2

import org.apache.spark.SparkContext

class DataBasic {

    protected val sc : SparkContext = GData.sc

    protected val bro_home : String = MyConfig.bro_home
    protected val bro_logs : String = "%s/logs".format(bro_home)

    protected var is_error : Boolean = false
    protected var msg_error : String = ""

    def getError : (Boolean, String) = {
        return (is_error, msg_error)
    }

}
