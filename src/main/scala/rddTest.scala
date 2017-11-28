import org.apache.spark.{SparkConf, SparkContext}
//test
object rddTest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local").setAppName("rddTest")
    val sc = new SparkContext(conf)
    val input = sc.parallelize(List(1,2,3,4,5,6,7,8,9,10,1,1,3,7,5,2,2,2,2))
    val result = input.map(x => x * x)
    println("*******************  println(result.collect().mkString(\" \")*********")
    println(result.collect().mkString(" "))

    /*return all element from RDD */
    println("** input.collect-return all element :"+input.collect())
    println("** input.count()-Number of elements in the RDD. :"+input.count())
    println("** input.countByValue()-Number of times each element occurs in the RDD. :"+input.countByValue())
    println("** input.take(3) :"+input.take(3))
    println("** input.top(2) :"+input.top(2))
    val orderedRDD = input.takeOrdered(3)(Ordering[Int].reverse)
    println("** input.takeOrdered(3)(Ordering[Int].reverse) : "+ orderedRDD)
    orderedRDD.foreach(println)
    println("^^ takeSmaple(false, 1) : "+ input.takeSample(false, 1))
    println("^^ takeSmaple(false, 1)-Return num elements at random : "+ input.takeSample(false, 1))
    println("*** input.reduce((x, y) => x + y) : "+ input.reduce((x, y) => x + y))
    println("*** input.fold(0)((x, y) => x + y) : "+ input.fold(0)((x, y) => x + y))



    /*(Ordering[Int].reverse.on(x => x.age))  */


  }

}
