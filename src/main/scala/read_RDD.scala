import org.apache.spark.sql.SparkSession

object read_RDD {
  def main(args: Array[String]): Unit = {
    val sparkSession_RD = SparkSession.builder
      .master("local")
      .appName("read RDD")
      .getOrCreate()
    val lines =sparkSession_RD.sparkContext.textFile("C:\\Users\\Gloria\\learning-spark\\data\\twinkle\\sample.txt")
    //val lines = sc.textFile("README.md") // Create an RDD called lines
  //  println(" ***** line of file : "+lines.count())
    println(" ***** line count of file : "+lines.count)
    println(" ***** first line of file : "+lines.first)
  }
}
