#!/bin/bash

sudo docker run -d -p 127.0.0.1:43574:8080 -t andystanton/opt-loc > pidfile
