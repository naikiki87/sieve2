#!/bin/bash 

# Revised: 2010/12/28, by tad :::

SERVER_IP=165.132.145.41
SERVER_NAME=deployermonitoring_sieve_user_0
SERVER_PORT=40010

# -------------------------------------------------------------------------
DASIE_HOME=`pwd`/..
DIST_DIR=${DASIE_HOME}/dist
CODE_BASE=file:${DIST_DIR}/
MAIN_CLASS="dblab.core.server.deployer.IEDeployerImpl"

clear

pids=$(ps -ef | grep -E $CODE_BASE'*.*'$MAIN_CLASS | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo 'kill '$pids': '$CODE_BASE'*.*'$MAIN_CLASS
	kill -9 ${pids} 2> /dev/null
fi
cd $DIST_DIR
java -classpath .:bin:./lib/* -Djava.library.path=. -Djava.rmi.server.hostname=$SERVER_IP -Djava.security.policy=server.policy -Djava.rmi.server.codebase=$CODE_BASE $MAIN_CLASS --server_name=$SERVER_NAME --server_port=$SERVER_PORT &
cd ..


exit 0

