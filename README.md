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
find . -name *.jar | xargs java -jar
```

An example endpoint will now be available through a Spray route running on (http://localhost:8080).

### Docker

A Dockerfile is available for creating a deployable Docker image. This requires Docker >= 1.0.0 to be running.

If you're [running Docker through boot2docker with VirtualBox on OSX](https://docs.docker.com/installation/mac/) you need to modify the network configuration for boot2docker's NAT adapter and forward port host 8080 to guest 8080 for 127.0.0.1.

```
git clone https://github.com/andystanton/opt-loc.git && cd opt-loc
sbt assembly
docker build -t andystanton/opt-loc .
docker run -d -p 8080:8080 andystanton/opt-loc
```
