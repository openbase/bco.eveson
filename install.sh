#!/bin/bash
APP_NAME='eveson'
clear &&
echo "=== clean ${APP_NAME} ===" &&
mvn clean $@ &&
clear &&
echo "=== install and deploy ${APP_NAME} ===" &&
mvn install -DassembleDirectory=${prefix} -Daudio.resource.folder=${prefix}/share/audio/samples/eveson -Dconfig.folder=${prefix}/etc/eveson $@ &&
clear &&
echo "=== ${APP_NAME} is successfully installed to ${prefix} ==="
