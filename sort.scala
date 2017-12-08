
/*Quick sort  */
object firstQuckSort{
  def main(args: Array[String]): Unit = {
  def sort(xs: Array[Int]) {
    def swap(i: Int, j: Int) {
      val t = xs(i); xs(i) = xs(j); xs(j) = t
    }
    def sort1(l: Int, r: Int) {
      val pivot = xs((l + r) / 2)
      var i = l; var j = r
      while (i <= j) {
        while (xs(i) < pivot) i += 1
        while (xs(j) > pivot) j -= 1
        if (i <= j) {
          swap(i, j)
          i += 1
          j -= 1
        }
      }
      if (l < j) sort1(l, j)
      if (j < r) sort1(i, r)
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