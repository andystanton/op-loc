# Optimum Locum [![Build Status](http://drone.cyniq.com/api/badge/github.com/andystanton/opt-loc/status.svg?branch=master)](http://drone.cyniq.com/github.com/andystanton/opt-loc)

API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* sbt >= 0.13.5
* docker >= 1.0

## Quick Start



```sh
git clone https://github.com/andystanton/opt-loc.git

cd opt-loc

docker build -t andystanton/optloc-db database

CID=$(docker run -d -p 5432:5432 andystanton/optloc-db)

echo "Postgres IP: "$(docker inspect --format '{{ .NetworkSettings.IPAddress }}' ${CID})
```

Update ```src/main/resources/opt-loc.properties``` with the Postgres IP. Then run the application:

```
sbt run
```

The application will be available on [http://localhost:8080](http://localhost:8080).

## Features

### Application

* Search for locations by name

### API

* Search for locations by name
* Get location information by id
* Search for locations in a radius around another location optionally filtered by population
