FROM    ubuntu:14.04

# install deps
RUN     locale-gen en_GB.utf8
RUN     apt-get update -qq \
            && apt-get -y -q install postgresql-9.3 \
                                     postgresql-client-9.3 \
                                     postgresql-9.3-postgis-scripts \
                                     postgresql-9.3-postgis-2.1 \
            && apt-get -y -q install unzip curl

# add content
ADD     ./ /database
RUN     chown postgres database

# init db
USER    postgres
RUN     echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf
RUN     echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf


# start
RUN     /etc/init.d/postgresql start \
            && psql -c 'CREATE EXTENSION postgis' \
            && /database/setup.sh
EXPOSE  5432
CMD     ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]