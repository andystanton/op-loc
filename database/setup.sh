#!/bin/bash

pushd `dirname $0` > /dev/null
SCRIPTPATH=`pwd -P`
popd > /dev/null

echo -e "=========================================================================="
echo -e "Creating Optimum Locum database and user..."
echo -e "=========================================================================="

psql < "$SCRIPTPATH/db-create.sql"

echo -e "=========================================================================="
echo -e "Preparing Optimum Locum database..."
echo -e "=========================================================================="

psql < "$SCRIPTPATH/db-prepare.sql" -d 'opt-loc'

echo -e "=========================================================================="
echo -e "Initiating Optimum Locum database..."
echo -e "=========================================================================="

psql < "$SCRIPTPATH/db-init.sql" -d 'opt-loc'

echo -e "=========================================================================="
echo -e "Populating Optimum Locum database..."
echo -e "=========================================================================="

echo -e "Downloading http://download.geonames.org/export/dump/GB.zip to data/GB.zip"

mkdir -p "$SCRIPTPATH/data/GB"

curl -o "$SCRIPTPATH/data/GB.zip" http://download.geonames.org/export/dump/GB.zip --silent

echo -e "Extracting data/GB.zip to data/GB"

unzip -o -q "$SCRIPTPATH/data/GB.zip" -d "$SCRIPTPATH/data/GB"

echo -e "Importing raw GB places data"

psql -d 'opt-loc' -c "copy raw_places_gb FROM '""$SCRIPTPATH""/data/GB/GB.txt' NULL AS ''"

echo -e "Importing relevant GB places data"

psql < "$SCRIPTPATH/db-copy-relevant.sql" -d 'opt-loc'

echo -e "=========================================================================="
echo -e "Database created, prepared initialised and populated"
echo -e "=========================================================================="