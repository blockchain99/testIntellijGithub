import org.apache.spark.{SparkConf, SparkContext}

object rddTest2 {
  def main(args: Array[String]): Unit = {
    val conf2 = new SparkConf().setMaster("local").setAppName("rddTest2")
    val sc2 = new SparkContext(conf2)
    case class Person(name: String, age: Int)
    val people = Array(Person("bob", 30), Person("ann", 32), Person("carl", 19), Person("Park", 39))
    val rdd = sc2.parallelize(people, 3)  //when not from external file, but from this program file.
    val orderedRdd = rdd.takeOrdered(3)(Ordering[Int].reverse.on(x => x.age))
    println("*** rdd.takeOrdered(3) : "+orderedRdd)
    orderedRdd.foreach(println)

    for (i <- 1 to people.length) {
      println("--- rdd.takeOrdered(%d) : ".format(i))
      sc2.parallelize(people, i).takeOrdered(i)(Ordering[Int].reverse.on(x => x.age)).foreach(println)
      println("* end of rdd.takeOrdered(%d) *** ".format(i))
    }
  }
}
