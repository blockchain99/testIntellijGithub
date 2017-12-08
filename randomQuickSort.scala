object randomQuickSort {

    // pivots randomly as opposed to mid point, to avoid degenerate O(n2) behaviour
    // will of course emit different debug on each run, but with same result

    def sort(seq:Seq[Int]):Seq[Int]={
      if (seq.size<2) return seq
      val pivotPos=scala.util.Random.nextInt(seq.size) // the only difference from prev QuickSort.scala
      val pivot=seq.apply(pivotPos)
      val (left,right)=seq.patch(pivotPos,Nil,1).partition(_< pivot) // take out pivot before we partition

      println(left,pivot,right)

      (sort(left):+ pivot) ++ sort(right)
    }                                               //> sort: (seq: Seq[Int])Seq[Int]

  def main(args: Array[String]): Unit = {
    val v=Vector(3,8,2,5,1,4,7,6)                   //> v  : scala.collection.immutable.Vector[Int] = Vector(3, 8, 2, 5, 1, 4, 7, 6)
    //|

    sort(v)                                         //> (Vector(3, 2, 1),4,Vector(8, 5, 7, 6))
    //| (Vector(2, 1),3,Vector())
    //| (Vector(),1,Vector(2))
    //| (Vector(5, 6),7,Vector(8))
    //| (Vector(),5,Vector(6))
    //| res0: Seq[Int] = Vector(1, 2, 3, 4, 5, 6, 7, 8)

    sort(Vector())                                  //> res1: Seq[Int] = Vector()

    sort(Vector(3))                                 //> res2: Seq[Int] = Vector(3)

    sort(Vector(99,99,99,98,10,-3,2))               //> (Vector(-3),2,Vector(99, 99, 99, 98, 10))
    //| (Vector(98, 10),99,Vector(99, 99))
    //| (Vector(),10,Vector(98))
    //| (Vector(),99,Vector(99))
    //| res3: Seq[Int] = Vector(-3, 2, 10, 98, 99, 99, 99)

    sort(Vector(1,2,3,4,5))                        //> (Vector(1, 2, 3),4,Vector(5))
    //| (Vector(1),2,Vector(3))
    //| res4: Seq[Int] = Vector(1, 2, 3, 4, 5)

  }//end of main
}
