# Optimum Locum
#
# VERSION 0.1.0
#
# DOCKER-VERSION 1.0.

FROM    ubuntu:14.04

RUN     apt-get update

RUN     apt-get -y install openjdk-7-jre

ADD     ./target/scala-2.11/opt-loc.jar opt-loc.jar

EXPOSE  8080

CMD     ["java", "-jar", "opt-loc.jar"]
