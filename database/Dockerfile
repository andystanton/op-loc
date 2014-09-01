# Optimum Locum Database
#
# VERSION 1.0
#
# DOCKER-VERSION 1.2.0

FROM    andystanton/docker-postgis
RUN     locale-gen --no-purge en_GB.UTF-8
ENV     LC_ALL en_GB.UTF-8
RUN     update-locale LANG=en_GB.UTF-8
RUN     apt-get -y -q install curl unzip
ADD     ./ /
RUN     service postgresql start && ./setup-docker.sh
CMD     ["./start.sh"]