#!/bin/bash 

# Revised: 2010/12/28, by tad :::

SERVER_IP=165.132.145.41
SERVER_NAME=planDescriptormonitoring_sieve_user_0
SERVER_PORT=40012

# -------------------------------------------------------------------------
DASIE_HOME=`pwd`/..
DIST_DIR=${DASIE_HOME}/dist
CODE_BASE=file:${DIST_DIR}/
MAIN_CLASS="dblab.core.server.plandescriptor.PlanDescriptorImpl"

clear

while ((1))
do
	pids=$(ps -ef | grep $MAIN_CLASS | grep -v grep | awk '{print $2}')

	if [ -n "${pids}" ]
	then
		kill -9 ${pids} 2> /dev/null
	fi

	cd $DIST_DIR

	java -classpath .:bin:./lib/* -Djava.library.path=. -Djava.rmi.server.hostname=$SERVER_IP -Djava.security.policy=server.policy -Djava.rmi.server.codebase=$CODE_BASE $MAIN_CLASS --server_name=$SERVER_NAME --server_port=$SERVER_PORT

	cd ..

done

exit 0
