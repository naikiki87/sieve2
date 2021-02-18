import os
import psutil
import subprocess
import time
import sys
import pymysql

sieve_db = pymysql.connect(
    user='root', 
    passwd='Elql27!^', 
    host='3.34.91.188', 
    db='cell_test2', 
    charset='utf8'
)

cursor = sieve_db.cursor(pymysql.cursors.DictCursor)

HOST = "165.132.105.40"

def update_watchdog(timestamp, lis_port, cpu_usage, mem_usage) :
    sql = 'UPDATE job_tasks SET cpu_usage=' + str(cpu_usage) + ' WHERE ec_id=' + str(ec_id) + ' AND listening_port=' + str(lis_port)
    cursor.execute(sql)
    sieve_db.commit()

sql = 'SELECT id FROM engine_computer where ip_address="' + HOST + '"'
cursor.execute(sql)
ec_id = (cursor.fetchall())[0]['id']


# while True :
pids = []
pair = {}

for proc in psutil.process_iter() :
    try :
        if proc.name() == 'python3.6' :
            print(proc.pid)
            # print(proc.cmdline()[3])
            port = proc.cmdline()[3]
            pids.append(proc.pid)


            # if proc.cmdline()[3] != null :
            #     pids.append(proc.pid)
            #     # if "watchdog.py" in proc.cmdline() :
            #     # print(proc.pid, proc.cmdline(), type(proc.cmdline()))
            #     # print(proc.pid, proc.cmdline()[3])
            pair[proc.pid] = port
    except :
        pass

# print("pids : ", pids)
# print("pair : ", pair)

for pid in pids :
    cmd = ['ps', '-p', str(pid), '-o', '%cpu'] 
    fd_popen = subprocess.Popen(cmd, stdout=subprocess.PIPE).stdout 
    data = fd_popen.read().strip() 
    fd_popen.close()

    data = data.decode('utf-8')
    data = data.split('\n')
    lis_port = pair[pid]
    cpu_usage = data[1]
    print(pid, lis_port, cpu_usage)
    # update_watchdog(1, lis_port, cpu_usage, 10)

time.sleep(1)
