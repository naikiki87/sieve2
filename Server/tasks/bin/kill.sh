#!/bin/bash 

EXEDESC="exedesc.sh"
EXEQUERY="exequery.sh"
EXEPLAN="exeplan.sh"
EXEDEP="exedep.sh"
IEDESC="IEDescriptorImpl"
IEQUERY="QueryDescriptorImpl"
IEPLAN="PlanDescriptorImpl"
IEDEP="IEDeployerImpl"

###
echo "killing descriptor..."
pids=$(ps -ef | grep $EXEDESC | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

pids=$(ps -ef | grep $IEDESC | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

###
echo "killing query descriptor..."
pids=$(ps -ef | grep $EXEQUERY | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

pids=$(ps -ef | grep $IEQUERY | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

###
echo "killing plan descriptor..."
pids=$(ps -ef | grep $EXEPLAN | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

pids=$(ps -ef | grep $IEPLAN | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

###
echo "killing deployer..."
pids=$(ps -ef | grep $EXEDEP | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

pids=$(ps -ef | grep $IEDEP | grep -v grep | awk '{print $2}')

if [ -n "${pids}" ]
then
	echo "kill $pids"
	kill -9 ${pids} 2> /dev/null
fi

exit 0
