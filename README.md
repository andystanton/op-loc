# Optimum Locum [![Build Status](https://travis-ci.org/andystanton/opt-loc.svg?branch=master)](https://travis-ci.org/andystanton/opt-loc)

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* scala >= 2.11.1
* sbt >= 0.13.5

## Quick Start

You will need a [Google Places API key](https://developers.google.com/places/documentation/index).

Clone the project:

```
git clone https://github.com/andystanton/opt-loc.git && cd opt-loc
```

Copy ```src/main/resources/opt-loc.properties.example``` to ```src/main/resources/opt-loc.properties``` and update the value of the property ```google.places.api.key``` to your Google Places API key.

### sbt

Start sbt

```
sbt
```

From inside sbt, start up the server using the Revolver plugin re-start command (re-stop shuts it down):

```
re-start
```

### Standalone

Alternatively, you can use sbt to generate a standalone runnable jar using the assembly plugin. From outside sbt:

```
sbt assembly
```

Try it out:

```
java -jar target/scala-2.11/opt-loc.jar
```

A location search endpoint will now be available on [http://localhost:8080/find/london](http://localhost:8080/find/london).

### Docker

A Dockerfile is available for creating a deployable Docker image. This requires Docker >= 1.0.1.

Having built the standalone application above and verified you are able to run it, you will be able to build a Docker image as follows: 

```
docker build -t andystanton/opt-loc .
docker run -d -p 8080:8080 andystanton/opt-loc
```

## Features

* Query Google Places API for a single location
