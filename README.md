# Optimum Locum [![Build Status](https://travis-ci.org/andystanton/opt-loc.svg?branch=master)](https://travis-ci.org/andystanton/opt-loc)

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* scala >= 2.11.1
* sbt >= 0.13.5
* postgresql >= 9.3
* postgis >= 2.1.3

## Quick Start

You will need to have postgresql installed and running with postgis support. Clone the project and initialise the database:

```
git clone https://github.com/andystanton/opt-loc.git
cd opt-loc/database
./setup.sh
cp src/main/resources/opt-loc.properties.example src/main/resources/opt-loc.properties
```

If your postgres database runs on anything other than ```localhost:5432``` update ```src/main/resources/opt-loc.properties``` with the correct hostname and port.

### sbt

From inside sbt, start up the server using the Revolver plugin re-start command (re-stop shuts it down):

```
~re-start
```

A location search endpoint will now be available on [http://localhost:8080/find/london](http://localhost:8080/find/london).

## Features

* Query Google Places API for a single location
