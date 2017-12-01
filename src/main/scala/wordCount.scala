import org.apache.spark.sql.SparkSession

object wordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder
      .master("local")
      .appName("wordcound")
      .getOrCreate()
    spark.sparkContext.setLogLevel("ERROR")// remove INFO message during run-time
    val lines = spark.sparkContext.textFile(
      "C:\\scala_coursera\\lec4_Big Data Analysis with Scala and Spark\\1\\testIntellijGithub\\src\\main\\data\\twinkle\\sample.txt")
    val word = lines
      .flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_+_)
    println("------- word : "+word)

    val totalChar = lines
      .map(_.length)
      .reduce(_+_)
    println("*********totalChar : "+ totalChar)


    val result2 = lines
      .map(x => (x.split(" ")(0), x))
    println("**** result2 :"+result2)
  }
}
