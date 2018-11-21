#!/bin/bash

for eachfile in /app/*.crt
    do
        echo Importing $eachfile
        NAME=$(echo "$eachfile" | sed 's/\/app\///g' | sed 's/\.crt//g')
        DECODED="$NAME-decoded.crt"
        echo $NAME $DECODED
        cat $eachfile|base64 -d > $DECODED && keytool -import -noprompt -trustcacerts -file $DECODED -alias $NAME -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit && echo "done importing $eachfile"
    done

rm  /app/*.crt
rm  /app/import-cacerts.sh