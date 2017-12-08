object conciseQuickSort {
  def main(args: Array[String]): Unit = {
    def sort(xs: Array[Int]): Array[Int] = {
      if (xs.length <= 1) xs
      else {
        val pivot = xs(xs.length / 2)
        Array.concat(
          sort(xs filter (pivot >)),  //sort(xs filter (pivot > _)),
          xs filter (pivot ==),       //xs filter (pivot == _),
          sort(xs filter (pivot <)))  //sort(xs filter (pivot < _)))
      }
    }//end of def sort

    /*(4, 11, 3, 9, 8, 10, 12)
      sort(4,3,8)::9::sort(11,10,12)
      (sort()::3::sort(4,8)) :: 9 :: (sort()::10::sort(11,12))
      3::(sort(4)::8::sort())::9::(10::sort(11)::12)
      3::4::8::9::10::11::12
      * * * */
    /* instead call xs.filter 3times, just one, which reduce run time*/
    def sortUp(ls: List[Int]): List[Int] = {
      ls match {
        case Nil => Nil
        case pivot :: tail => {
          val (less, greater) = tail.partition(_ < pivot)
          sortUp(less) ::: pivot :: sortUp(greater)
        }
      }
    }

    val arr = Array.fill(100000) { scala.util.Random.nextInt(100000 - 1) }
    var t1 = System.currentTimeMillis
    val sortedList2 = sort(arr)
    var t2 = System.currentTimeMillis
    println("Functional style with 3times filter & Array : " + (t2-t1))

//    val arr = Array.fill(100000) { scala.util.Random.nextInt(100000 - 1) }
    var tt1 = System.currentTimeMillis
    val sortedList3 = sortUp(arr.toList)
    var tt2 = System.currentTimeMillis
    println("Functional style with ::: List: " + (tt2-tt1))

    println("****** test second -----------------")
    val test = Array(4, 11, 3, 9, 8, 10, 12)
    test.foreach(x=>print(x+" "))
    println()

    var sortedArray = sort(test)
    print("** sorted result Array : ")
    sortedArray.foreach(x => print(x+" "))
    println
    val sortedList = sort(test).toList
    println("* sortedList.toString :"+sortedList.toString)
    /*mkString will convert collections(incl Array) element-by-element to string representations*/
    println(sortedList.mkString(" "))
    /*Array.foreach(println)    */
    sortedList.foreach(x=>print(x + " "))
    println()
    sortedList.foreach(print _)
    println()
    sortedList.foreach(print)
  }//end of main
}//end of object conciseQuickSort
