#!/bin/bash
misc/liquibase/liquibase --classpath=misc/libs/h2-1.4.185.jar --driver=org.h2.Driver --changeLogFile=misc/database/changelog.xml --url=jdbc:h2:./misc/database/app --username=sa --password=sa update