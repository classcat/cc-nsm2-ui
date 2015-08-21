
package com.classcat.ccnsm2

import org.apache.spark.SparkContext

// import com.classcat.ccnsm2.CCConfig

class DataCurrConnTcp () { // sc : SparkContext) {

    val sc = GData.sc

    println("IN DataCurrConnTcp contructor")
    println(MyConfig.myip)
    println(MyConfig.bro_home)

}
