//test
import org.apache.spark.sql.SparkSession

object tw_Dataset {
  def main(args: Array[String]): Unit = {
    val sparkSession_tw = SparkSession.builder
      .master("local")
      .appName("Twinkle line")
      .getOrCreate()
    val b =sparkSession_tw.read.textFile("C:\\Users\\Gloria\\learning-spark\\data\\twinkle\\sample.txt")

    println("************* line of sample is :" +b.count)
    // 5
    val b_twinkle = b.filter(_.contains("twinkle"))
    // b_twinkle: org.apache.spark.sql.Dataset[String]
    println("****** line number with filtered with word twinkle: "+b_twinkle.count)

    // 2
    println("******************print each line ******************")
    b_twinkle.collect.foreach(println)
    // twinkle twinkle little star
    // twinkle twinkle little star
  }
}
