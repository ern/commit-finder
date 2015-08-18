#!/bin/bash

while read line
do
    if ! $( git cat-file -e $line ); then
        printf "%s\n" "$line SHA is invalid"
    fi
done < "${1:-/dev/stdin}"
