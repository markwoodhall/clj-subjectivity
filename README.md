# clj-subjectivity

A Clojure wrapper around a subjectivity lexicon [from here](http://mpqa.cs.pitt.edu/)

## Status

[![Build Status](https://api.travis-ci.org/markwoodhall/clj-subjectivity.svg?branch=master)](https://api.travis-ci.org/repositories/markwoodhall/clj-subjectivity)

## Usage

Pass a sequence of words to the `sentiment` function like so:

```clojure
(sentiment ["fire"])
```

This will return a map similar to:

```clojure
{:difference -0.5,
 :negative 0.5,
 :neutral 0,
 :positive 0,
 :top-negative '("fire"),
 :top-neutral '(),
 :top-positive '()
 :bottom-negative '("fire")
 :bottom-neutral '()
 :bottom-positive '()}
```

`positive`, `negative`, and `neutral` are counts of the respective word types.
`difference` is the `positive` count minus the `negative` count.
`top-nagative`, `top-positive`, and `top-neutral` are the most frequently occuring words for the respective work type.
`bottom-nagative`, `bottom--positive`, and `bottom-neutral` are the least frequently occuring words for the respective work type.

## License

Copyright © 2016 Mark Woodhall

Released under the MIT License: http://www.opensource.org/licenses/mit-license.php
