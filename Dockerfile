# Optimum Locum
#
# VERSION 0.1.0
#
# DOCKER-VERSION 1.0.

FROM    ubuntu:12.04

RUN     apt-get update -qq

RUN     apt-get -y -q install wget curl python-software-properties software-properties-common

RUN     wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -

RUN     echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" >> /etc/apt/sources.list.d/postgresql.list

RUN     add-apt-repository -y ppa:ubuntugis/ubuntugis-unstable

RUN     apt-get update -qq

RUN     locale-gen en_GB.utf8

RUN     apt-get -y -q install openjdk-7-jre

RUN     apt-get -y -q install postgresql-9.3 postgresql-client-9.3 postgresql-9.3-postgis-scripts libgdal1 postgresql-9.3-postgis-2.1

ADD     ./database /database

ADD     ./target/scala-2.11/opt-loc.jar opt-loc.jar

RUN     chown postgres database

USER    postgres

RUN     /etc/init.d/postgresql start \
            && psql -c 'CREATE EXTENSION postgis' \
            && ./database/setup.sh

USER    root

EXPOSE  8080

CMD     /etc/init.d/postgresql start && java -jar opt-loc.jar
