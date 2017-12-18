object rationals {
  class Rational(x: Int, y: Int) {
    require(y != 0, "denominator must be nonzero")

    def this(x:Int) = this(x, 1)

    private def gcd(a: Int, b: Int): Int = {
      if (b==0) a else gcd (b, a % b)
    }
    def numer = x
    def denom = y

    def less(that: Rational) = numer * that.denom < that.numer * denom

    def max(that: Rational) = if (this.less(that)) that else this

    def add(that: Rational) =
      new Rational(
        numer * that.denom + that.numer * denom,
        denom * that.denom
      )

    def neg():Rational = {
      new Rational(-numer, denom)
    }

    def sub(that: Rational) = {
      add(that.neg)
    }

    override def toString() = {
      val g = gcd(x, y)
      numer/g + "/" + denom/g
    }
  }
  val x = new Rational(500000000, 1000000000)
  val y = new Rational(6, 12)
  y.add(y)


}

