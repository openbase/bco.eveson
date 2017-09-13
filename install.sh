#!/bin/bash
APP_NAME='eveson'
clear &&
echo "=== clean ${APP_NAME} ===" &&
mvn clean --quiet $@ &&
clear &&
echo "=== install ${APP_NAME} to ${prefix} ===" &&
mvn install \
        -DassembleDirectory=${prefix} \
        -DskipTests=true \
        -Dmaven.test.skip=true \
        -Dlicense.skipAddThirdParty=true \
        -Dlicense.skipUpdateProjectLicense=true \
        -Dlicense.skipDownloadLicenses \
        -Dlicense.skipCheckLicense=true \
        -Dmaven.license.skip=true \
        -Daudio.resource.folder=${prefix}/share/audio/samples/eveson \
        -Dconfig.folder=${prefix}/etc/eveson \
        --quiet $@ &&
clear &&
echo "=== ${APP_NAME} is successfully installed to ${prefix} ==="
