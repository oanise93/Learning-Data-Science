# Week 2 Video Notes

## Lecture 3.1 - Class Hierarchies
### Abstract Classes

Consider the task of writing a class for sets of integers with the following operators
```scala
abstract class IntSet {
    def incl(x: Int): IntSet
    def contains(x: Int): Boolean
}
```
`IntSet` is an _**abstract class**__.

Abstract classes can contain members which are missing an implementation (in our case, `incl` and `contains`).

Consequently, no instances of an abstract class can be created with the operatore `new`.

### Class Extensions

Let's consider implementing sets as binary trees.

There are two types of possible trees: a tree for the empty set, and a tree consisting of an integer and two sub-trees.

Here are their implementations

```scala
class Empty extends IntSet {
    def contains(x: Int): Boolean = false
    def incl(x: Int): IntSet = new NonEmpty(x, new Empty, new Empty)
}
```

### Class Extensions (2)

```scala
class NonEmpty(elem: Int, left: IntSet, right: IntSet) extends IntSet {
    
    def contains(x: Int): Boolean = {
        if (x < elem) left contains x
        else if (x > elem) right contains x
        else true
    }

    def incl(x: Int): IntSet = {
        if (x < elem) new NonEmpty(elem, left incl x, right)
        else if (x > elem) new NonEmpty(elem, left, right incl x)
        else this
    }
}
```

### Terminology 
`Empty` and `NonEmpty` both _**extend**_ the class `IntSet`.

This implies that the types `Empty` and `NonEmpty` _**conform**_ to the type `IntSet`.
* an object of type `Empty` or `NonEmpty` can be used wherever an object of type `IntSet` is required.

### Base Classes and Subclasses

`IntSet` is called the _**superclass**_ of `Empty` and `NonEmpty`.

`Empty` and `NonEmpty` are _**subclasses**_ of `IntSet`.

In Scala, any user-defined class extends another class.

If no superclass is given, the standard class `Object` in the Java package `java.lang` is assumed.

The direct or indirect superclasses of a class `C` are called _**base classes**_ of `C`.

So, the base classes of `NonEmpty` are `IntSet` and `Object`.

### Implementation and Overriding

The definitions of `contains` and `incl` in the classes `Empty` and `NonEmpty` _**implement**_ the abstract functions in the base trait `IntSet`. 

It is also possibel to _**redefine**_ an existing, non-abstract definition in a subclass by using `override`.

#### Example

```scala
abstract class Base {
    def foo = 1
    def bar: Int
}
```

```scala
class Sub extends Base {
    override def foo = 2
    def bar = 3
}
```

### Object Definitions

In the `IntSet` example, one could argue that there is really only a single empty `IntSet`.

So it seems overkill to have the user create many instances of it.

We can express this case better with an _object definition_:
```scala
object Empty extends IntSet {
    def contains(x: Int): Boolean = false
    def incl(x: Int): IntSet = new NonEmpty(x, Empty, Empty)
}
```

This defines a _**singleton object**_ named `Empty`.

No other `Empty` instances can (or need to be) created.

Singleton objects are values, so `Empty` evaluates to itself.

### Programs

So far we have executed all Scala code from the REPL or the worksheet.

But is also possible to create standalone applications in Scala.

Each such application contains an object with a `main` method.

For instance, here is the "Hello World!" program in Scala.
```scala
object Hello {
    def main(args: Array[String]) = println("hello world!")
}
```
Once this program is compiled, you can start it from the command line with
* `scala Hello`

### Exercise

Write a method `union` for forming the union of two sets. You should implement the following abstract class:

```scala
  abstract class IntSet {
    def incl(x: Int): IntSet
    def contains(x: Int): Boolean
    def union(other: IntSet): IntSet
  }
```

### Dynamic Binding

Object-oriented langauges (including Scala) implement _**dynamic method dispatch**_

This means that the code invoked by a method call depends on the runtime of the object that contains the method

#### Example
`Empty contains 1`

$\longrightarrow$ `[1/x] [Empty/this] false`

`= false`


### Dynamic Binding (2)

Another evaluation using `NonEmpty`:

`(new NonEmpty(7, Empty, Empty)) contains 7`

$\longrightarrow$ ` [7/elem] [7/x] [new NonEmpty (7, Empty, Empty)/this]`
```scala
if (x < elem) this.left contains x
    else if (x > elem) this.right contains x else true
```

`= if(7 < 7) new NonEmpty(7, Empty, Empty).left contains 7`   
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `else if (7> 7) new NonEmpty(7, Empty, Empty).right contains 7 else true`

$\longrightarrow$ `true`

## Lecture 3.2 - How Classes Are Organized
### Packages
Classes and objects are organized in packages

To place a class or object inside a package, usae a package clause at the top of your source file.
```scala
package progfun.examples

object Hello {...}
```
This would place `Hello` in the package `progfun.examples`. 

You can then refer to `Hello` by its _fully qualified name_ `progfun.examples.Hello`. For instance to run the `Hello` program:

```shell
scala progfun.examples.Hello
```

### Forms of Imports

Imports come in several forms:

```scala
import week3.Rational // imports just Rational
import week3.{Rational, Hello} // imports both Rational and Hello
import week3._ // imports everything in package week3
```
The first two forms are called _named imports_.

The last form is called a _wildcard import_.

You can import from either a package or an object.

### Automatic Imports
Some entities are automatically imported in any Scala Program.

These are:
* All members of package `scala`
* All members of package `java.lang`
* All members of the singleton object `scala.Predef`

Here are the fully qualified names of some types and functions which you have seen so far:
types | fully qualified name 
--- | ---
`Int` | `scala.Int`
`Boolean` | `scala.Boolean`
`Object` | `java.lang.Object`
`require` | `scala.Predef.require`
`assert` | `scala.Predef.assert`

### Traits

In Java, as well as Scala, a class can only have one super class (single inheritance).

But what if a class has several natural supertypes to which it conforms or from which it wants to inherit code?

Here, you could use `traits`.

A trait is declared like an abstract class, just with `trait` instead of `abstract class`.

```scala
trait Planar {
    def height: Int
    def width: Int
    def surface = height * width
}
```

### Traits (2)

Classes, objects and traits can inherit from at most one class but arbitrary many traits

Example:
```scala
class Square extends Shape with Planar with Movable ...
```

Traits resemble interfaces in Java, but are more powerful because they can contains fields and concrete methods.

On the other hand, traits cannot have (value) parameters only classes can.

### Top Types

At the top of the type hiearchy we find:
<html>
    <table>
        <tr>
            <td>Any</td>
            <td>the base type of all types </br> 
            Methods: '==', '!=', 'equals', 'hashCode', 'toString'
            </td>
        </tr>
        <tr>
            <td>AnyRef</td>
            <td>The base type of all reference types </br>
            Alias of 'java.lang.Object
            </td>
        </tr>
        <tr>
            <td>AnyVal</td>
            <td>The base type of all primitive types </td>
        </tr>
    </table>
</html>

### The Nothing Type

`Nothing` is at the bottom of Scala's type hierarchy. It is a subtype of every other type.

There is no value of type `Nothing`.

Why is that useful?
* To signal abnormal termination
* As an elemnt type of empty collections (see next session)

### Exceptions
Scala's exception handling is similar to Java's

The expression
```scala
throw Exc
```

aborts evaluation with the exception `Exc`

The type of this expression is `Nothing`.


### The Null Type

Every reference class type also has `null` as a value

The type of `null` is `Null`

`Null` is a subtype of every class that inherits from `Object`; it is incompatible with subtypes of `AnyVal`.

```scala
val x = null // x: Null
val y: String = null // y: String
val z: Int = null //error: type mismatch
```

### Exercise

What is the type of 
```scala
if (true) 1 else false
```
[] `Int`   
[] `Boolean`   
[x] `AnyVal`   
[] `Object`   
[] `Any`   

## Polymorphism

### Cons-Lists
A fundamental data structure in many functional languages is the immuatable linked list.

It is constructed from two building blocks:
* `Nil` the empty list
* `Cons` a cell containing an element and the remainder of the list

### Cons-List in Scala

Here's an outline of a class hiearchy that represents lists of integers in this fashion:
```scala
package

trait IntList ...
class Cons(val head: Int, val tail: IntList) extends IntList ...
class Nil extends IntList ...
```
A list is either
* an empty list `new Nil`, or 
* a list `new Cons(x, xs)` consisting of a `head` element `x` and a `tail` list `xs`.

### Value Parameters

Note the abbreviation `(val head: Int, val tail: IntList)` in the definition of `Cons`.

This defines at the same time parameters and fields of a class.

It is equivalent to:

```scala
class Cons(_head: Int, _tail: IntList) extends IntList {
    val head = _head
    val tail = _tail
}
```
where `_head` and `_tail` are otherwise unused names.

### Type Parameters
It seems too narrow to define only lists with `Int` elements.

We'd need another class hierarchy for `Double` lists, and so on, one for each possible element type.

We can generalize the definition using a `type` parameter:

```scala
package week4

trait List[T]
class Cons[T](val head: T, val tail: List[T]) extends List[T]
class Nil[T] extends List[T]
``` 
Type parameters are written in square brakcets, e.g. `[T]`.

### Complete Definition of List

```scala
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
```

### Generic Functions
Like classes, functions can have type parameters

For instance, here is a function that creates a list consisting of a single element. 

```scala
def singleton[T](elem: Int) = new Cons[T](elem, new Nil[T])
```

We can then write:
```scala
singleton[Int](1)
singleton[Boolean](true)
```

### Type Inference

In fact, the Scala compiler can usually deduce the correct type parameters from the value arguments of a function call.

So, in most cases, type parameters can be left out. You could also write:

```scala
singleton(1)
singleton(true)
``` 

### Types and Evaluation

Type parameters do not affect evaluation in Scala

We can assume that all type parameters and type arguments are removed before evaluating the program.

This is also called **_type erasure_**.

Languages that use type erasure include Java, Scala, Haskell, ML, OCaml.

Some other languages keep the type parameters around at run time, these include C++. C#, F#.

### Polymorphishm

Polymorphism means that a function type comes "in many forms."

In programming it means that
* the function can be applied to argumetns of many types, or 
* the type can have instances of many types.

We have seen two principal forms of polymorphishm
* subtyping: instances of a subclass can be passed to a base class
* generics: instances of a function or class are created by type parameterization
