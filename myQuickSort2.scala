object myQuickSort2 {

    def partition(a: Array[Int], low: Int, high: Int): Int = { //def, when return value, "=" is need before {
      def swap(i: Int, j: Int) {
        val t = a(i)
        a(i) = a(j)
        a(j) = t
      }

      val pivot = a(high)
      var i = low - 1

      for (j <- 0 to high - 1) {
        if (a(j) <= pivot) {
          i += 1
          swap(i, j)
          //                      println("interim sorted result is :" +a.mkString(" "))
        }
      }
      swap(i + 1, high)
      i + 1
      //return i+1 //same as above
    }

    /*low ---> starting index, high ---> ending index  */
    def sort(a: Array[Int], low: Int, high: Int): Unit = {
      if (low < high) {
//        /*a[partionInde] is now at right place  */
//        var partitionIndex = partition(a, low, high)
        /*recursively sort elements before partition and after partition  */
        sort(a, low, partition(a, low, high) - 1)
        sort(a, partition(a, low, high) + 1, high)
      }
    }
  def main(args: Array[String]): Unit = {
    val testArray = Array(4, 11, 3, 9, 8, 10, 12)
    println(testArray.mkString(" "))

    var n = testArray.length

    sort(testArray, 0, n-1)
    println("Sorted result is : ")
    testArray.foreach(x => print(x + " "))


  } //end of main
}//end of object myQuckSort


