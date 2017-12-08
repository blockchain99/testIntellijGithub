object classQuicksort {
  class QuickSort {

    def sort(xs: Array[Int]): Array[Int] = {
      xs.length < 2 match {
        case true => xs
        case false => {
          val pivot = xs(xs.length / 2)
          sort(xs filter (pivot > _)) ++ Array(pivot) ++ sort(xs filter (pivot < _))
        }
      }
    }

    def sort(xs : List[Int]) : List[Int] = {
      xs match {
        case x :: Nil => xs  //prepend single item x to List Nil
        case x :: ys => {
          val pivot = x
          sort (ys filter (pivot > _)) ::: List(pivot) ::: sort (ys filter (pivot < _))  //prepend whole List
        }
        case Nil =>  Nil
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val studentScores = ('a' to 'z').map(x => (x, (Math.random() * 100).toInt))
    for(e <- studentScores)
        print(e+" ")
    println()
    val score = studentScores.map(_._2)

    val sortedArray = new QuickSort().sort(score.toArray)
    println("^ sortedArray with for loop: ")
    for(e <- sortedArray)
      print(e+" ")
    println()
    val sortedList = new QuickSort().sort(score.toList)
    println("* sortedArray :"+sortedArray)
    println("- sortedList :"+sortedList)
  }



}
