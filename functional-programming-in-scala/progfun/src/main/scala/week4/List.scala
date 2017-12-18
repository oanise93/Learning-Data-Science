package week4

import java.util.NoSuchElementException

trait List[T] {
  def isEmpty: Boolean = ???
  def head: T = ???
  def tail: List[T] = ???
}

class Cons[T](override val head: T, override val tail: List[T]) extends List[T] {
  override def isEmpty:Boolean = false
}

class Nil[T] extends List[T]{
  override def isEmpty: Boolean = true
  override def head:Nothing = throw new NoSuchElementException("Nil.head")
  override def tail:Nothing = throw new NoSuchElementException("Nil.tail")
}