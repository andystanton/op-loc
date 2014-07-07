#!/bin/bash

sbt assembly

sudo docker build -t andystanton/opt-loc .
