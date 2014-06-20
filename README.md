# Optimum Locum [![Build Status](https://travis-ci.org/andystanton/opt-loc.svg?branch=master)](https://travis-ci.org/andystanton/opt-loc)

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* Scala >= 2.11.1
* sbt >= 0.13.5

## Quick Start

```
git clone https://github.com/andystanton/opt-loc.git && cd opt-loc
sbt clean assembly
find . -name *.jar | xargs java -jar
```

An example endpoint will now be available through a Spray route running on http://localhost:8080.
