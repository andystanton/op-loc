# Optimum Locum [![Build Status](https://travis-ci.org/andystanton/opt-loc.svg?branch=master)](https://travis-ci.org/andystanton/opt-loc)

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* scala >= 2.11.1
* sbt >= 0.13.5

## Quick Start

### Standalone

```
git clone https://github.com/andystanton/opt-loc.git && cd opt-loc
sbt assembly
java -jar target/scala-2.11/opt-loc.jar
```

An example endpoint will now be available through a Spray route running on [http://localhost:8080](http://localhost:8080).

### Docker

A Dockerfile is available for creating a deployable Docker image. This requires Docker >= 1.0.0.

```
git clone https://github.com/andystanton/opt-loc.git && cd opt-loc
sbt assembly
docker build -t andystanton/opt-loc .
docker run -d -p 8080:8080 andystanton/opt-loc
```
