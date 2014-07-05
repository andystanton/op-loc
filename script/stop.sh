#!/bin/bash

#sudo docker ps -a | awk '/andystanton\/opt-loc.*Up / {print $1}' | xargs sudo docker kill 

cat pidfile | xargs sudo docker kill
