#!/bin/bash

if [ $# != 3 ]; then
	echo "Usage: revise.sh <ipaddr> <rmi-suffix> <start port-number>"
	exit 1
fi

ipaddr=$1
suffix=$2
portnum=$3
servers=servers.xml

##--------------------------------------------------------------------
echo "revising exedep.sh, (${ipaddr} : deployer${suffix} : ${portnum})..."
sed -i "s/^SERVER_IP=\([^ ]*\)/SERVER_IP=${ipaddr}/
        s/^SERVER_NAME=deployer\([^ ]*\)/SERVER_NAME=deployer${suffix}/
        s/^SERVER_PORT=\([^ ]*\)/SERVER_PORT=${portnum}/" exedep.sh

##--------------------------------------------------------------------
let "portnum=$3+1"

echo "revising exedesc.sh, (${ipaddr} : descriptor${suffix} : ${portnum})..."
sed -i "s/^SERVER_IP=\([^ ]*\)/SERVER_IP=${ipaddr}/
        s/^SERVER_NAME=descriptor\([^ ]*\)/SERVER_NAME=descriptor${suffix}/
        s/^SERVER_PORT=\([^ ]*\)/SERVER_PORT=${portnum}/" exedesc.sh

##--------------------------------------------------------------------
let "portnum=$3+2"

echo "revising exeplan.sh, (${ipaddr} : planDescriptor${suffix} : ${portnum})..."
sed -i "s/^SERVER_IP=\([^ ]*\)/SERVER_IP=${ipaddr}/
        s/^SERVER_NAME=planDescriptor\([^ ]*\)/SERVER_NAME=planDescriptor${suffix}/
        s/^SERVER_PORT=\([^ ]*\)/SERVER_PORT=${portnum}/" exeplan.sh

##--------------------------------------------------------------------
let "portnum=$3+3"

echo "revising exequery.sh, (${ipaddr} : queryDescriptor${suffix} : ${portnum})..."
sed -i "s/^SERVER_IP=\([^ ]*\)/SERVER_IP=${ipaddr}/
        s/^SERVER_NAME=queryDescriptor\([^ ]*\)/SERVER_NAME=queryDescriptor${suffix}/
        s/^SERVER_PORT=\([^ ]*\)/SERVER_PORT=${portnum}/" exequery.sh


##--------------------------------------------------------------------
touch ${servers}
echo "creating ${servers}..."
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" > ${servers}
echo "<!-- Created on : `date` -->" >> ${servers}
echo "<server_info>" >> ${servers}
echo "  <server_ip>${ipaddr}</server_ip>" >> ${servers}
echo "  <accounter>accounter</accounter>" >> ${servers}
echo "  <deployer>deployer${suffix}</deployer>" >> ${servers}
echo "  <descriptor>descriptor${suffix}</descriptor>" >> ${servers}
echo "  <plandescriptor>planDescriptor${suffix}</plandescriptor>" >> ${servers}
echo "  <querydescriptor>queryDescriptor${suffix}</querydescriptor>" >> ${servers}
echo "</server_info>" >> ${servers}

echo "done"

exit 0

