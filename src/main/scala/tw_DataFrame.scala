import org.apache.spark.sql.SparkSession

//test
object tw_DataFrame {

  def main(args: Array[String]): Unit = {
    val sparkSession_tw = SparkSession.builder
      .master("local")
      .appName("Twinkle line")
      .getOrCreate()
    /**sparkSession.read.text : DataFrame */
    val c =sparkSession_tw.read.text("C:\\Users\\Gloria\\learning-spark\\data\\twinkle\\sample.txt")  //DataFrame
    /* saprkSession.sparkContext.textFile : RDD   */
    val lines =sparkSession_tw.sparkContext.textFile("C:\\Users\\Gloria\\learning-spark\\data\\twinkle\\sample.txt") //RDD
    /* sparkSession.read.textFile : Dataset  */
    /* val conf=SparkConf.setMaster("...").setAppName("...")
     * val sc = new SparkContext(conf) */
    println("************* c.printSchema : *****")
    c.printSchema  //DataFrame
    println("------------------------------------")
    c.show(false)
    println("*** c.count : "+c.count)
    println("^^^^^^^^^^^^   value like '%twinkle%' ^^^^^^^^^^^^^^^^")
    val c_twinkle = c.filter("value like '%twinkle%'")
    c_twinkle.show(false)
    println()
    println(" ***** line count of file : "+lines.count)   //RDD
    println(" ***** first line of file : "+lines.first)   //RDD
    println("version : 20171201")
  }

}
