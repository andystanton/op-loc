#!/bin/bash

pushd `dirname $0` > /dev/null
SCRIPTPATH=`pwd -P`
popd > /dev/null

$SCRIPTPATH/stop.sh
$SCRIPTPATH/build.sh
$SCRIPTPATH/start.sh
