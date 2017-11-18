package recfun

import scala.collection.mutable.Stack

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
    def pascal(c: Int, r: Int): Int = {
      if ((c==r) || (c == 0))
        1
      else
        pascal(c-1, r-1) + pascal(c, r-1)
    }
  
  /**
   * Exercise 2
   */
    def balance(chars: List[Char]): Boolean = {
      def balanceIter(stack: Stack[Char], chars: List[Char]): Boolean = {
        if (chars.isEmpty && !stack.isEmpty){
          false
        }
        else if (chars.isEmpty && stack.isEmpty) {
          true
        }
        else if (chars.head == ")".head && stack.isEmpty) {
          false
        }
        else if (chars.head != ")".head && chars.head != "(".head) {
          balanceIter(stack, chars.tail)
        }
        else if (chars.head == ")".head && stack.top == "(".head) {
          stack.pop()
          balanceIter(stack, chars.tail)
        }
        else {
          balanceIter(stack.push(chars.head), chars.tail)
        }
      }

      balanceIter(Stack[Char](), chars)
    }
  
  /**
   * Exercise 3
   */
    def countChange(money: Int, coins: List[Int]): Int = {
      if (money == 0)
        1
      else if (coins.isEmpty || money < 0)
        0
      else {
        countChange(money, coins.tail) + countChange(money - coins.head, coins)
      }

    }
  }
