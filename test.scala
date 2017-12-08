object test {
  def main(args: Array[String]): Unit = {
    val list1 = List(1,2)
    val list2 = List(3,4)

    println("*list1::list2: "+ (list1::list2))  //prepend single item to first List
    println("**list1:::list2: "+ (list1:::list2)) //prepend a complete list
  }
}
