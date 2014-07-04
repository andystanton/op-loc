# Optimum Locum Database
#
# VERSION 0.1.0
#
# DOCKER-VERSION 1.0.

FROM    helmi03/docker-postgis

RUN     locale-gen --no-purge en_GB.UTF-8

ENV     LC_ALL en_GB.UTF-8

RUN     update-locale LANG=en_GB.UTF-8

RUN     apt-get -y -q install wget unzip

ADD     ./ /

RUN     ./start.sh