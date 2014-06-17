# Optimum Locum

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* Scala >= 2.11.0
* sbt >= 0.13.5

## Quick Start

```
git clone https://github.com/andystanton/op-loc.git && cd op-loc
sbt clean assembly
find . -name *.jar | xargs java -jar
```

An example Scalatra servlet will now be available at http://localhost:8080 and an example Akka job will be ticking over in the background.

