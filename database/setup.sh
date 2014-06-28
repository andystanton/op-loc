#!/bin/bash

echo -e "=========================================================================="
echo -e "Creating Optimum Locum database and user..."
echo -e "=========================================================================="

psql < db-create.sql

echo -e "=========================================================================="
echo -e "Preparing Optimum Locum database..."
echo -e "=========================================================================="

psql < db-prepare.sql -d 'opt-loc'

echo -e "=========================================================================="
echo -e "Initiating Optimum Locum database..."
echo -e "=========================================================================="

psql < db-init.sql -d 'opt-loc'

echo -e "=========================================================================="
echo -e "Populating Optimum Locum database..."
echo -e "=========================================================================="

echo -e "Downloading http://download.geonames.org/export/dump/GB.zip to data/GB.zip"

curl -o data/GB.zip http://download.geonames.org/export/dump/GB.zip --silent

echo -e "Extracting data/GB.zip to data/GB"

mkdir -p data/GB
unzip -o -q data/GB.zip -d data/GB

echo -e "Importing raw GB places data"

psql -d 'opt-loc' -c "copy raw_places_gb FROM '""$PWD""/data/GB/GB.txt' NULL AS ''"

echo -e "Importing relevant GB places data"

psql < db-copy-relevant.sql -d 'opt-loc'

echo -e "=========================================================================="
echo -e "Database created, prepared initialised and populated"
echo -e "=========================================================================="