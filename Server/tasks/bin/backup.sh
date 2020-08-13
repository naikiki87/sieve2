#!/bin/bash

dasie_home=`pwd`/..

today=`date '+%Y%m%d-%H%M%S'`
cron_conf_cur=${dasie_home}/dist/config
cron_conf_bak=${dasie_home}/backup/config-${today}.tar.gz
cron_src_algo_cur=${dasie_home}/project/algo
cron_src_algo_bak=${dasie_home}/backup/project-algo-${today}.tar.gz
cron_src_auth_cur=${dasie_home}/project/auth
cron_src_auth_bak=${dasie_home}/backup/project-auth-${today}.tar.gz
cron_src_calgo_cur=${dasie_home}/project/calgo
cron_src_calgo_bak=${dasie_home}/backup/project-calgo-${today}.tar.gz
cron_src_iec_cur=${dasie_home}/project/iec
cron_src_iec_bak=${dasie_home}/backup/project-iec-${today}.tar.gz
cron_src_server_cur=${dasie_home}/project/server
cron_src_server_bak=${dasie_home}/backup/project-server-${today}.tar.gz

tar cvzf ${cron_conf_bak} ${cron_conf_cur} >& /dev/null
#chown -R dasie:dblab ${cron_conf_bak}

tar cvzf ${cron_src_algo_bak} ${cron_src_algo_cur} >& /dev/null
#chown -R dasie:dblab ${cron_src_bak}
tar cvzf ${cron_src_auth_bak} ${cron_src_auth_cur} >& /dev/null
#chown -R dasie:dblab ${cron_src_bak}
tar cvzf ${cron_src_calgo_bak} ${cron_src_calgo_cur} >& /dev/null
#chown -R dasie:dblab ${cron_src_bak}
tar cvzf ${cron_src_iec_bak} ${cron_src_iec_cur} >& /dev/null
#chown -R dasie:dblab ${cron_src_bak}
tar cvzf ${cron_src_server_bak} ${cron_src_server_cur} >& /dev/null
#chown -R dasie:dblab ${cron_src_bak}

exit 0
