
/*Quick sort  */
object firstQuckSort{

  def main(args: Array[String]): Unit = {

  def sort(xs: Array[Int]) {
    def swap(i: Int, j: Int) {
      val t = xs(i); xs(i) = xs(j); xs(j) = t
    }

    def sort1(left: Int, right: Int) {
      val pivot = xs((left + right) / 2)
      var i = left; var j = right
      while (i <= j) {
        while (xs(i) < pivot) i += 1
        while (xs(j) > pivot) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (left < j) sort1(left, j)
      if (j < right) sort1(i, right)
    }

    sort1(0, xs.length - 1)
  }

    val test = Array(4, 11, 3, 9, 8, 10, 12)
    println(test)
    test.foreach(x=>print(x+" "))
    println("===>")
    sort(test)  /* Once sorted, the sorted result is stored  */
    test.foreach(x=> print(x+" "))
//    /*mkString will convert collections(incl Array) element-by-element``` to string representations*/
//    println(sortedList.mkString(" "))
//    /*Array.foreach(println)    */
//    sortedList.foreach(x=>print(x + " "))
  }//end of main
}//end of object firstQuickSort