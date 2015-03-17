# Optimum Locum [![Build Status](http://drone.cyniq.com/api/badge/github.com/andystanton/opt-loc/status.svg?branch=master)](http://drone.cyniq.com/github.com/andystanton/opt-loc)

Web application & API for evaluating the suitability of candidate locations based on configurable variables.

## Requirements

* sbt >= 0.13.7
* docker >= 1.5
* docker-compose >= 1.1.0

## Quick Start

```sh
git clone https://github.com/andystanton/opt-loc.git
cd opt-loc
cp src/main/resources/opt-loc.properties.example src/main/resources/opt-loc.properties
sbt assembly
docker-compose up
```

The application will be available on ```http://<DOCKER_HOST_IP>:43574```.

```<DOCKER_HOST_IP>``` is usually localhost on Linux and the default value is something like 192.168.59.103 when using boot2docker.

## Features

### Application

* Search for locations by name

### API

* Search for locations by name
* Get location information by id
* Search for locations in a radius around another location optionally filtered by population
