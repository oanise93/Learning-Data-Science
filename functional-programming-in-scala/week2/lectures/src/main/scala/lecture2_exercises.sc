object Exercise{
  // Exercise 1
//  def product(f: Int => Int)(a: Int, b:Int): Int = {
//    if (a > b) 1
//    else f(a) * product(f)(a+1, b)
//  }
//
//  product(x => x)(3, 4)

  // Exercise 2
  def fact(a: Int): Int = {
    product(x => x)(1, a)
  }

  fact(5)

  // Exercise 3
  def mapReduce(f: Int => Int, op: (Int, Int) => Int, zeroValue: Int)(a: Int, b: Int): Int = {
    if (a > b) zeroValue
    else op(f(a), mapReduce(f, op, zeroValue)(a+1, b))
  }

  def product(f: Int => Int)(a: Int, b:Int): Int = mapReduce(f, (x, y) => x * y, 1)(a, b)

  product(x => x * x)(3, 4)
}