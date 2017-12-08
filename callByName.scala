object callByName {
  /*call by name  : =>
  * unreduced arguments.*/
  def incIfZeroCN(y: => Int): Int = if(y == 0) y+1 else y
  /*CV:reduces function arguments to values be-
fore rewriting the function application.  */
  def incIfZeroCV(y: Int): Int = if(y == 0) y+1 else y

  def something() = {
    println("calling something")
    1 // return value
  }
  def callByValue(x: Int) = {
    println("x1 ="+ x)
    println("x2 ="+ x)
  }
  def callByName(x: => Int) = {
    println("x1 ="+ x)
    println("x2 ="+ x)
  }

  def main(args: Array[String]): Unit = {
    var x1 = 0
    println("IncIfZero by name : ")
    var x1R = incIfZeroCN({val tmp = x1; x1 += 1; tmp }) //CN(code is transfer) so, result caculated by code is transfer
    println(x1R)

    var x2 = 0
    println("IncIfZero by value : ")
    var x2R = incIfZeroCV({val tmp = x2; x2 += 1; tmp })  //CV(value only transfer) tmp is 0, then tmp: 0 is input value
    /* tmp = 0, tmp assigned 0 , so 0 is transferred to def  */
    println(x2R)

    println("---------------------------")
    println("callByName(something() : ")
    callByName(something())
    println("callByValue(something() :")
    callByValue(something())
  }
}
