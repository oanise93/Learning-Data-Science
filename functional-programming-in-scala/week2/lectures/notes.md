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
Take the sum of the factorials of all the integers between a and b:

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
### Finding a fixed point of a function
A number is `x` is a called a fixed point of a function `f` if 
$$ f(x) = x$$

For some functions `f` we can locate the fixed points by starting with an initial estimate and then applying `f` in a repetitive way.
$$x, f(x), f(f(x)), f(f(f(x))), ...$$
until the value does not vary anymore (or the change is sufficiently small )

### Programmatic Solution
```scala
val tolerance = 0.0001
def isCloseEnough(x: Double, y: Double) = 
  abs((x - y) / x) / x < tolerance
def fixedPoint(f: Double => Double)(firstGuess: Double) = {
  def iterate(guess: Double): Double = {
    val next = f(guess)
    if (isCloseEnough(guess, next)) next
    else iterate(firstGuess)
  }
  iterate(next)
}
```

### Return to Square Roots
Here is a _specification_ of the `sqrt` function:   
`sqrt(x)` = the number `y` such that `y * y = x`   
Or, by dividing both sides of the equation with `y`:   
`sqrt(x)` = the number `y` such that `y = x / y`   
Consequently, `sqrt(x)` is a fixed point of the function `(y => x / y)`

### First Attempt
This suggests to calculate `sqrt(x)` by iteration towards a fixed point:

```scala
def sqrt(x: Double) = 
  fixedPoint(y => x / y)(1.0)
``` 

Unfortunately, this does not converge

### Functions as Return Values
The previous exmplaes have that that the expressive power of a language is greately increased if we can pass function arguments

The following example shows that functions that return functions can also be very useful

Consider again iteration towards a fixed point

We bgin by observing that $\sqrt(x)$ is a fixed boy of the function `y => x / y`.

Then, the iteration converged by averaging successive values.

This technique of _stabilizing by averaging_ is general enough to merit being abstracted into its own function.
```scala
def averageDamp(f: Double => Double)(x: Double) = (x + f(x)) / 2
```

### Exercise
Write a square root function using `fixedPoint` and `averageDamp`.
## Lecture 2.4 - Scala Syntax Summary

### Language Elements Seen So Far:

We hav eseen language elemtns to express types, expressions and defintions

Below, we give their context-free syntax in Extended Backus-Naur form (EBNF), where
* | denotes an alternative
* [...] an option (0 or 1)
* {...} a repetition (0 or more)

### Types 
Type = SimpleType | FunctionType
Function Type = SimpleType '=>' Type | '(' [Types] ')' '=>' Type
SimpleType = Ident
Types = Type {',' Type}

A **type** can be:   
* A **numeric type**: Int, Double (and `Byte`, `Short`, `Char`, `Long`, `Float`)
* The `Boolean` type with the values `true` and `false`
* The `String` type
* A **function type**, like `Int => Int, (Int, Int) => Int`.

### Expression

An _**express**_ can be:
* An identifier such as `x`, `isGoodEnough`,
* A literal like, `0`, `1.0`, `"abc"`
* A function application, like `sqrt(x)`,
* An operation application, like `-x`, `y+x`,
* A selection, like `math.abs`
* A `conditional expression`, like `if (x < 0) -x else x`
* A block, like `{ val x = math.abs(y); x * 2}`
* An anonymous function, like `x => x + 1`

### Definitions

A definition can be
* A function definition, like `def square(x: Int) = x * x`
* A value definition, like `val y = square(2)`

A parameter can be: 
* A call-by-value parameter, like (x: Int)
* A call-by-name parameter, like (y: => Double)

## Lecture 2.5 - Functions and Data

### Functions and Data

In this section, we'll learn how functions create and encapsulate data structures.

#### Example
Rational Numbers

We want to design a package for doing rational arithemetic.

A rational number $x/y$ is represented by two integers:
* its number $x$, and
* its denominator $y$.

### Rational Addition

Suppose we weant to implement the addition of two rational numbers.
```scala
def addRationalNumerator(n1: Int, d1: Int, n2: Int, d2: Int): Int
def addRationalDenominator(n1: Int, d1: Int, n2: Int, d2: Int): Int
```

but it would be difficult to manage all these numerators and denominators. 

A better choice is to combine the numerator and denominator of a rational number in a data structure.

### Classes 
In Scala, we do this by defining a class:

```scala
class Rational(x: Int, y: Int) {
  def numer = x
  def denom = y
}

This defintion introduced two entities:
* A new type, named Rational
* A constructor Rational to create elements of this type

Scala keeps the names of types and values in different namespaces.   
So there's no conflict between the two defintions of `Rational`.
```

### Objects
We call the elements of a class type objects

We create an object by prefixing an application of the constructor of the class with the operator new.

#### Example

`new Rational(1, 2)

### Rational Arithmetic
We can now define the arithmetic functions that implement the standard rules

$$n1/d1 + n2/d2 = (n1d2 + n2d1)/d1d2$$
$$n1/d1 - n2/d2 = (n1d2 - n2d1)/d1d2$$
$$n1/d1 * n2/d2 = n1n2/d1d2$$
$$(n1/d1) / (n2/d2) = n1d2/d1n2$$
$n1/d1 = n2/d2$ iff  $n1d2 = d1n2$

### Implementing Rational Arithmetic

```scala
def addRational(r: Rational, s:Rational): Rational = 
  new Rational(
    r.numer * s.denom + s.numer * r.deonm, r.denom * s.denom
  )

def makeString(r:Rational) = 
  r.numer + "/" + r.denom
```
`makeString(addRational(new Rational(1, 2), new Rational(2, 3)))
` > 7/6

### Methods

One can go fruther and also package functiosn operating on a data abstraction in the data abstraction itself

Such functions are called _**methods**_
#### Example
Rational numbers now would have, in addition to the functions `numer` and `denom`, the functions `add`, `sub`, `mul`, `div`, `equal`, `toString`.

### Calling Methods

Here is how one might use the `Rational` abstraction:

```scala
val x = new Rational(1, 3)
val y = new Rational(5, 7)
val z = new Rational(3, 2)
x.add(y).mul(z)
```

### Exercise
1. In your worksheet, add a method neg to class Rational that is used like this:
`x. neg // evaluates to -x`
2. Add a method `sub` to subtract two rational numbers
3. With the values of x, y, z as given in the previous slide, what is the result of
`x - y - z`?


## Lecture 2.6 - More Fun with Rationals

### Data Abstraction
The previous example has shown that rational numbers aren't always represented in their simplest form. (Why?)

One would expect the rational numbers to be _simplified_:
* reduce them to their smallest numerator and denominator by dividing both with a divisor

We could implement this in each rational operation, but it would be easy to forget this division in an operator.

A better alternative consists of simplifying the representation in the class when the objects are constructed.

### Rational with Data Abstraction

```scala
  class Rational(x: Int, y: Int) {
    private def gcd(a: Int, b: Int): Int = {
      if (b==0) a else gcd (b, a % b)
    }
    private val g = gcd(x, y)

    def numer = x / g
    def denom = y / g
    ...
  }
```

`gcd` and `g` are _**private**_ members; we can only access them from inside the `Rational` clas.

In this example, we calculate `gcd` immediately, so that its value can be re-used in the calculations of `numer` and `denom`.

### Rational with Data Abstraction (2)
It is also possible to call `gcd` in the code of `numer` and `denom`:
```scala
  class Rational(x: Int, y: Int) {
    private def gcd(a: Int, b: Int): Int = {
      if (b==0) a else gcd (b, a % b)
    }

    def numer = x / gcd(x, y)
    def denom = y / gcd(x, y)
  }
```
This can be advatageous if it is expected that the functions `numer` and `denom` are called infrequently.

### Rationals with Data Abstraction (3)
It is equally possible to turn `numer` and `denom` into `vals`, so that they are computed only once:

It is also possible to call `gcd` in the code of `numer` and `denom`:
```scala
  class Rational(x: Int, y: Int) {
    private def gcd(a: Int, b: Int): Int = {
      if (b==0) a else gcd (b, a % b)
    }

    val numer = x / gcd(x, y)
    val denom = y / gcd(x, y)
  }
```
This can be advatageous if it is expected that the functions `numer` and `denom` are called often.

### The Client's View
Clients observe exactly the same behavior in each case. 

This ability to choose different implementations of the data without affecting clients is called _data abstraction_.

It is a cornerstone of software engineering.

### Self Reference
On the inside of a class, the name `this` represents the object on which the current method is executed.

#### Example
Add the functions `less` and `max` to the class `Rational`.

```scala
  class Rational(x: Int, y: Int) {
    ...

    def less(that: Rational) = numer * that.denom < that.numer * denom

    def max(that: Rational) = if (this.less(that)) that else this
  }
```

### Selef Reference (2)
Note that a simple `x`, which refers to anoter member of the class, is an abbreviation of `this.x`. Thus, an equivalent way to formulate `less` is as follows.

```scala
def less(that: Rational) =
  this.numer * that.denom < that.numer * this.denom
```

### Preconditions

Let's say our `Rational` class that the denominator is positive.

We can enforce this by calling the `require` function.

```scala
  class Rational(x: Int, y: Int) {
    require(y != 0, "denominator must be nonzero")
    ...
  }
```

`require` is a predefined function.

It takes a condition and an optonal message string.

If the condition passed to `require` is `false`, an `IllegalArgumentException` is thrown with the given message string.

### Assertions
Besides `require`, there is also `assert`.

Assert also takes a condition and an optional message string as parameters. E.g.
```scala
val x = sqrt(y)
assert (x >= 0)
```
Like `require` a failing `assert` will also throw an excpetion, but it's a different one: `AssertionError` for `assert`, `IllegalArgumentException` for `require`.

This reflect a difference in intent.
* `require` is used to enforce a precondition on the caller of a function.
* `assert` is used as to check the code of the function itself.

### Constructors
In Scala, a class implicitly introduces a constructor. This one is called the _**primary constructor**_ of the class.

The primary constructor 
* take the parameters of the class
* and executes all statements in the class body (such as the `require` a couple slides back.)

### Exercise
Modify the `Rational` class so that rational numbers are kept unsimplified internally, but the simplification is applied when numbers are converted to string.

Do clients observe the same behavior when interactional with the `Rational` class?
* [] yes
* [] no 
* [x] yes for small sizes of denominators and nominators and small number of operations
* [] none of the above
## Lecture 2.7 - Evaluation and Parameters 
### Classes and Substitution 

We previously defined the meaning of a function application using a computation model based on substitution. Now we extend this model to classes and objects.

Question: How is an instantion of the class new C(e<sub>1</sub>, ..., e<sub>m</sub>)

Answer: The expression arguments e<sub>1</sub>, ..., e<sub>m</sub> are evaluated like the arguments of a normal function. That's it

The resulting expression, say new C(v<sub>1</sub>, ..., v<sub>m</sub>) is already a value

Now suppose that we have a class definition,

<p style="text-align: center;">class C(x<sub>1</sub>, ..., x<sub>m</sub>) { ... def f(y<sub>1</sub>, ..., y<sub>m</sub>) = b ...} </p>

where 
* The formal parameters of the class are x<sub>1</sub>, ..., x<sub>m</sub>
* The class defines a formal method `f` with formal parameters y<sub>1</sub>, ..., y<sub>n</sub>

(The list of function parameters can be absent. For simplicity, we have ommitted the parameter types.)

_**Question**_: How is the following expression evaluated?
<p style="text-align: center;">new C(v<sub>1</sub>, ..., v<sub>m</sub>).f(w<sub>1</sub>, ..., w<sub>n</sub>)</p>

### Classes and Substitution (2)
_**Answer**_: The expression new C(v<sub>1</sub>, ..., v<sub>m</sub>).f(w<sub>1</sub>, ..., w<sub>n</sub>) is rewritten to:

<p style="text-align: center;">[w<sub>1</sub>/y<sub>1</sub>, ..., w<sub>m</sub>/y<sub>m</sub>][v<sub>1</sub>/x<sub>1</sub>, ..., v<sub>m</sub>/x<sub>m</sub>][new C(v<sub>1</sub>, ..., v<sub>m</sub>)/this]</p>

There are three subsitutions at work here:
* the substitution of the formal parameters y<sub>1</sub>, ..., y<sub>n</sub> of the function `f` by the arguments w<sub>1</sub>, ..., w<sub>n</sub>
* the substitution of the formal parameters x<sub>1</sub>, ..., x<sub>m</sub> of the class `C` by the class arguments v<sub>1</sub>, ..., v<sub>m</sub>
* the substitution of the self reference _this_ by the value fo the object new C(v<sub>1</sub>, ..., v<sub>n</sub>)

### Object Rewriting Examples

`new Rational(1, 2).numer`   
$\rightarrow$ [1/x, 2/y] [] [new Rational (1, 2)/this] x   
= 1 

`new Rational(1, 2).less(new Rational(2, 3))`   
$\rightarrow$ [1/x, 2/y] [newRational(2, 3)/that] [new Rational (1, 2)/this]   
= `new Rational(1,2).numer * new Rational(2, 3.).denom < 
     new Rational(1,2).denom * new Rational(2, 3.).numer`   
$\twoheadrightarrow$ 1 * 3 < 2 * 2   
$\twoheadrightarrow$ true

### Operators

In princple, the rational numbers defined by `Rational` are a natural as integers.

But for the user of these abstractions, there is a noticeable difference
* We write `x + y`, if `x` and `y` are integers, but
* We write `r.add(s)` if `r` and `s` are rational numbers

In Scala, we can eliminate this difference. We procede in two steps

### Step 1: Infix Notation
Any method with a parameter can be used like an infix operator.

It is therefore possible to write

`r add s` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `r.add(s)`   
`r less s` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  /* place of  */  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `r.less(s)`   
`r max s` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `r.max(s)`

### Step 2: Relaxed Identifiers
Operators can be used as identifiers

Thus, an identifier can be:
* Alphanumeric: starting with a letter, followed by a sequence of letters or numbers
* Symbolic: starting with an operator symbol, followed by other operator symbols
* The underscore character '_' counts as a letter.
* Alphanumeric identifiers can also end in an underscore, followed by some operator symbols.

Examples of identifiers:

`x1` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `*` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `+?%&` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `vector_++` &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; `counter_=`

### Operators for Rationals

... and rational numbers can be used like `Int` or `Double`:

```scala
val x = new Rational(1, 2)
val y = new Rational(1, 3)
x * x + y * y
```

### Precedence Rules
The precedence of an operator is determined by its first character.

The foloowing lists the characters in increaing order of priority precedence:
* (all letters)
* |
* ^
* &
* < >
* = !
* :
* '+' -
* '*' / %
* (all other special characters)

### Exercise

Provide a fully parenthesized version of 

`a + b ^? c ?^ d less a ==> b | c`

#### Solution
`(((a + b) ^? (c ?^ d)) less ((a ==> b) | c))`


