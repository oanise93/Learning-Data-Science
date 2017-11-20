# Week 2 Video Notes

## Lecture 2.1 - Higher Order Functions
### Higher Order Functions
* Functional languages treat functions as first-class values
* This means that, like any other value, a function can be passed as a parameter and returned as a result
* This provides a flexible way to compose programs
* Functions that take other functions as parameters or that return functions as results are called higher order functions
* first-order functions only act on simple data types

### Example
Take the sum of the integers between `a` and `b`:
```scala
def sumInt(a: Int, b: Int): Int = 
  if (a > b) 0 else a + sumInts(a + 1, b)
```

Take the sum of the cubes of all the integers between `a` and `b`:

```scala 
def cube(x: Int): Int = x * x * x
```

```scala 
def sumCubes(a: Int, b: Int): Int =
  if (a > b) 0 else cube(a) + sumCubes(a + 1, b)
```

### Example (ctd)
Take the sume of the factorials of all the integers between a and b:

```scala 
def sumFactorials(a: Int, b: Int): Int =
  if (a > b) 0 else fact(a) + sumFactorials(a + 1, b)
```

These are the special case of $\sum_{n=a}^{b} f(n)$ for different values of $f$.

Can we factor out the common pattern?

### Summing with Higher-Order Functions

Let's define:

```scala
def sum(f: Int => Int, a: Int, b: Int): Int =
  if (a > b ) 0
  else f(a) + sum(f, a + 1, b)
```

We can then write:

```scala
def sumInts(a:Int, b: Int) = 
  sum(id, a, b)   

def sumCubes(a:Int, b: Int) = 
  sum(cube, a, b)
 
def sumFactorials(a:Int, b: Int) = sum(fact, a, b)
```   

where  
```scala 
def id(x: Int): Int = x
def id(x: Int): Int = x * x * x
def fact(x: Int): Int = 
  if (x == 0) 1 else x * fact(x-1)
```

### Function Types
The type `A => B` is the type of _**function**_ that takes an argument of type `A` and returns a result of type `B`.

So, `Int => Int` is the type of functions that map integers to integers. 

### Anonymous Functions
Passing functions as parameters leads to the creation of many small functions
* Sometimes it is tedious to have to define (and name) these functions using `def`.   
Compare to string: We do not need to define a string using `def`. Instead of 
```scala
def str = "abc"; println(str)
```
We can directly write
```scala
println("abc)
```
because strings exists as _literals_. Analogously we would like function literals, which let us write a function without giving it a name.

These are called _anonymous functions_.

### Anonymous Function Syntax
**Example**: A function that raises its arguement to a cube

```scala
(x: Int) => x * x * x
```

Here, `(x: Int)` is the _parameter_ of the function, and `x * x * x` is it's _body_.   
* The type fot the parameter can be ommitted if it can be inferrered by the compiler from the context.   

If there are serveral parameters, they are seperated by 
```scala
(x: Int, y: Int) => x + y
```

### Summation with Anonymous Functions
Using anonuymous functions, we can write sums in a shorter way:
```scala
def sumInts(a: Int, b: Int) = sum(x => x, a, b)
def sumCubes(a: Int, b:Int) = sum(x => x * x * x, a, b)
```

### Exercise
Write a tail-recursive version of sum.

```scala
def sum(f: Int => Int, a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (???) ???
    else loop(???, ???)
  }
  loop(???, ???)
}
```

Solution:

```scala
def sum(f: Int => Int, a: Int, b: Int): Int = {
  def loop(a: Int, acc: Int): Int = {
    if (a > b) acc
    else loop(a + 1, f(a) + acc)
  }
  loop(a, 0)
}
```
## Lecture 2.2 - Currying

### Motivation
```scala
def sumInts(a: Int, b: Int) = sum(x => x, a, b)
def sumCubes(a: Int, b:Int) = sum(x => x * x * x, a, b)
def sumFactorials(a: Int, b:Int) = sum(fact, a, b)
```

#### Question   
Note that `a` and `b` get passed unchanged from `sumInts` and `sumCubes` into `sum`.   
Can we be even shorter by getting rid of those parameters?

### Functions Returning Functions

```scala
def sum(f:Int => Int): (Int, Int) => Int = {
  def sumF(a: Int, b: Int): Int = 
    if (a > b) 0
    else f(a) + sumF(a + 1, b)

  sumF
}
```
`sum` is now a function that returns another function.   
The returned function `sumF` applies the given function parameter `f` and sums the results.

### Stepwise Applications

We can then define:
```scala
def sumInts = sum(x => x)
def sumCubes = sum(x => x * x * x)
def sumFactorials = sum(fact)
```

These functictions can in turn be applied like any other function:   
`sumCubes(1, 10) + sumFactorials(10, 20)`

### Consecutive Stepwise Applications
In the previous example, can we avoid the `sumInts`, `sumCubes`, ... middlemen?
Of course:
`sum (cube) (1, 10)`
* `sum(cube)` applies `sum` to `cube` and returns the _sum of cubes_ function.
* `sum(cube)` is therefore requivalent to sumCubes
* This function is next applied to the arguments (1, 10).   

Generally, function application associates to the left:   
`sum(cube)(1, 10) == (sum (cube)) (1, 10)

### Multiple Parameter Lists
The defintiion of functions that return functiosn is so useful in functional programming that there is a special syntax for it in Scala.

For example, the following defintion of sum is equivalent to the one with the ensted `sumF` function, but shorter:
```scala
def sum(f: Int => Int)(a: Int, b: Int): Int = 
  if (a > b) 0 else f(a) + sum(f)(a+1, b)
```

### More function Types

Question: Give, 
```scala
def sum(f: Int => Int)(a: Int, b: Int): Int = ...
```
What is the type of `sum`?

#### Answer 
(Int => Int) => (Int, Int) => Int

Note that the functional types associate to the right. That is to say that   
`Int => Int => Int`

is equivalent to   
`Int => (Int => Int)`

### Exercise 
1. Write a `product` function that calculates the product of the values of a function for the points on a given interval.
2. Write `factorial` in terms of `product`.
3. Can you write a more general function, which generalized both sum and product
## Lecture 2.3 - Example Finding Fixed Points

## Lecture 2.4 - Scala Syntax Summary

## Lecture 2.5 - Functions and Data

## Lecture 2.6 - More Fun with Rationals

## Lecture 2.7 - Evaluation and Parameters 