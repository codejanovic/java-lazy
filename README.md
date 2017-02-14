[![Build Status](https://travis-ci.org/codejanovic/java-lazy.svg?branch=develop)](https://travis-ci.org/codejanovic/java-lazy)
[![Coverage Status](https://coveralls.io/repos/github/codejanovic/java-lazy/badge.svg?branch=develop)](https://coveralls.io/github/codejanovic/java-lazy?branch=develop)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg?maxAge=2592000)]()

# java-lazy
Very simple approach to achieve lazy initialization on desired instances.

## Maven
Release artifact
```xml
<dependency>
    <groupId>io.github.codejanovic</groupId>
    <artifactId>java-lazy</artifactId>
    <version>0.1.0</version>
</dependency>
```
Snapshot artifact
```xml
<dependency>
    <groupId>io.github.codejanovic</groupId>
    <artifactId>java-lazy</artifactId>
    <version>0.2.0-SNAPSHOT</version>
</dependency>
```

## Tell me about the Usecase
Well thats simple: whenever you need a lazy initialized value! :-) 

For me it became important when writing caching decorators. I wanted to let constructors short and fast, without rewriting the same code over and over again.

Let me show you what I am talking about ...

Lets assume you got some implementation of `Iterable<T>` doing some rocket-science-time-consuming stuff in `iterator()` method:

```java
final class TimeConsumingIterable implements Iterable<RocketScience> {
    private final List<RocketScience> rocketScienceList;
    
    public TimeConsumingIterable(final List<RocketScience> rocketScienceList) {
        this.rocketScienceList = rocketScienceList;
    }
    
    @Override
    public Iterator<RocketScience> iterator() {
        return rocketScienceList.stream()         
                .map(itToSomething())
                .map(itAgain())
                .flatMap(thatShit())
                .map(itBackAgain())
                .iterator();
    }
}
```
So far so good, but you don't want to do that timeconsuming stuff from `iterator()` method everytime it is called - so we need some sort of caching. As we don't want to bloat this neat little class with additional caching code, lets create a caching decorator instead:
```java
import io.github.codejanovic.java.lazy.Lazy;

final class CachingIterable<T> implements Iterable<T> {
    private final Lazy<Iterator<T>> lazyIterator;
    
    public CachingIterable(final Iterable<T> decorated) {
        this.lazyIterator = new Lazy.Cached<>(new Lazy.Value(decorated::iterator));
    }
    
    @Override
    public Iterator<T> iterator() {
        return lazyIterator.value();
    }
}
```
Et Voilà! We just created a generic caching decorator for `Iterable<T>` which keeps the constructor clean and fast, while deferring the expensive operations from `decorated.iterator()` method to the `iterator()` method of this decorator, as the decorated `iterator()` is instantiated and cached lazily.
 
## But, but ... why don't you just ...
> ... bind the iterator to an instance variable in `TimeConsumingIterable()` constructor like this?

```java
final class TimeConsumingIterable implements Iterable<RocketScience> {
    private final List<RocketScience> timeConsumingRocketScience;
    
    public TimeConsumingIterable(final List<RocketScience> rocketScienceList) {
        this.timeConsumingRocketScience = rocketScienceList.stream()         
                                      .map(itToSomething())
                                      .map(itAgain())
                                      .flatMap(thatShit())
                                      .map(itBackAgain())
                                      .collect(Collectors.toList());
    }
    
    @Override
    public Iterator<RocketScience> iterator() {
        return timeConsumingRocketScience.iterator();
    }
}
```

Because I think it violates two principles:
* to keep constructors cheap in execution
* to defer execution to the farthest point in time (which is when the code is actually needed)