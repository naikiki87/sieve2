import os
import psutil
import time
import pickle
import sys
import subprocess
import pymysql
from socket import *
import psutil

HOST = sys.argv[1]

def db_con() :
    conn = pymysql.connect(
        user='root', 
        passwd='Elql27!^', 
        host='165.132.105.40', 
        db='cell_test2', 
        charset='utf8'
    )
    return conn

conn = db_con()
cursor = conn.cursor(pymysql.cursors.DictCursor)

# sql = "SELECT * FROM engine_computer;"
# cursor.execute(sql)
# result = cursor.fetchall()

# print(result)
# conn.close()

sql = 'SELECT id FROM engine_computer where ip_address="' + HOST + '"'
cursor.execute(sql)
ec_id = (cursor.fetchall())[0]['id']
conn.close()

def update_watchdog(timestamp, cpu_usage, mem_usage, cpu_speed) :
    conn = db_con()
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    # sql = 'update watchdog set timestamp=' + str(timestamp) + ', cpu_usage=' + str(cpu_usage) + ', mem_usage=' + str(mem_usage) + ' where ip_addr="' + str(HOST) + '"'
    sql = 'update engine_computer set timestamp=' + str(timestamp) + ', cpu_usage=' + str(cpu_usage) + ', mem_usage=' + str(mem_usage) + ', cpu_speed=' + str(cpu_speed) + ' where ip_address="' + str(HOST) + '"'
    cursor.execute(sql)
    conn.commit()
    conn.close()

def task_watchdog(timestamp, lis_port, cpu_usage) :
    conn = db_con()
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    sql = 'UPDATE job_tasks SET cpu_usage=' + str(cpu_usage) + ' WHERE ec_id=' + str(ec_id) + ' AND listening_port=' + str(lis_port)
    cursor.execute(sql)
    conn.commit()
    conn.close()

while True :
    # pid = os.getpid()
    # py  = psutil.Process(pid)
    # cpu_usage   = os.popen("ps aux | grep " + str(pid) + " | grep -v grep | awk '{print $3}'").read()
    # cpu_usage   = float(cpu_usage.replace("\n",""))
    # mem_usage  = round(py.memory_info()[0] /2.**30, 2)
    try :

        cpu_usage = psutil.cpu_percent(interval=0.1, percpu=False)
        mem_usage = psutil.virtual_memory().percent
        timestamp = time.time()
        timestamp = int(timestamp)
        cpu_speed = int(psutil.cpu_freq().current)

        print("cpu : ", cpu_usage)

        update_watchdog(timestamp, cpu_usage, mem_usage, cpu_speed)

        pids = []
        pair = {}

        for proc in psutil.process_iter() :
            try :
                if proc.name() == "python3" or proc.name() == "python3.6":
                    port = proc.cmdline()[3]
                    pids.append(proc.pid)
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
            # print(pid, lis_port, cpu_usage)
            task_watchdog(1, lis_port, cpu_usage)
    except :
        pass

    time.sleep(1)