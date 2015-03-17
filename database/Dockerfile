# Optimum Locum
#
# VERSION 0.1.0
#
# DOCKER-VERSION 1.0.

FROM    ubuntu:12.04

# install deps
RUN     apt-get update -qq
RUN     apt-get -y -q install unzip wget curl python-software-properties software-properties-common
RUN     wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -
RUN     echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" >> /etc/apt/sources.list.d/postgresql.list
RUN     add-apt-repository -y ppa:ubuntugis/ubuntugis-unstable
RUN     apt-get update -qq
RUN     locale-gen en_GB.utf8
RUN     apt-get -y -q install postgresql-9.3 postgresql-client-9.3 postgresql-9.3-postgis-scripts libgdal1 postgresql-9.3-postgis-2.1

# add content
ADD     ./ /database
RUN     chown postgres database

# init db

USER    postgres

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf


# start

RUN     /etc/init.d/postgresql start \
            && psql -c 'CREATE EXTENSION postgis' \
            && /database/setup.sh
EXPOSE  5432
CMD     ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]