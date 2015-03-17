# Optimum Locum
#
# VERSION 0.1.0
#
# DOCKER-VERSION 1.0.

FROM    dockerfile/java:oracle-java7
ADD     ./target/scala-2.11/opt-loc.jar opt-loc.jar
EXPOSE  8080
CMD     java -jar opt-loc.jar
