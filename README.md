# Optimum Locum [![Build Status](https://travis-ci.org/andystanton/opt-loc.svg?branch=master)](https://travis-ci.org/andystanton/opt-loc)

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* sbt >= 0.13.5
* postgresql >= 9.3 + postgis >= 2.1
* bower >= 1.3.5

## Quick Start

You will need to have postgres installed and running with postgis support. Clone the project and initialise the database:

```
BASEDIR=$PWD/opt-loc && git clone https://github.com/andystanton/opt-loc.git $BASEDIR

# setup database
$BASEDIR/database/setup.sh

# setup properties
cp $BASEDIR/src/main/resources/opt-loc.properties.example $BASEDIR/src/main/resources/opt-loc.properties

# setup front end
cd $BASEDIR/src/main/resources/WEB-INF && bower update

# go back to thr project root. we can launch sbt from here
cd $BASEDIR
```

If your postgres database runs on anything other than ```localhost:5432``` update ```src/main/resources/opt-loc.properties``` with the correct hostname and port.

### sbt

From inside sbt, start up the server using the Revolver plugin re-start command (re-stop shuts it down):

```
~re-start
```

A location search will now be available on [http://localhost:8080](http://localhost:8080).

## Features

### Application

* Search for locations by name

### API

* Search for locations by name
* Get location information by id
* Search for locations in a radius around another location optionally filtered by population
