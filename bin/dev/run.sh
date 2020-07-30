#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
	echo  "Usage: $(basename "$0") {directory_with_csv_reports}"
	exit 1
fi

cd ../../
sbt "run $1"
