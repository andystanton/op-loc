#!/bin/bash

sbt assembly

if [[ ${OSTYPE} == "linux-gnu" ]]; then
    docker_cmd="sudo docker"
else
    docker_cmd="docker"
fi

${docker_cmd} build -t andystanton/opt-loc .
