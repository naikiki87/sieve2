import os
import psutil
import time
import pickle
import sys
import pymysql
from socket import *

sieve_db = pymysql.connect(
    user='root', 
    passwd='Elql27!^', 
    host='3.34.91.188', 
    db='cell_test2', 
    charset='utf8'
)

cursor = sieve_db.cursor(pymysql.cursors.DictCursor)

sql = "SELECT * FROM engine_computer;"
cursor.execute(sql)
result = cursor.fetchall()

print(result)

HOST = sys.argv[1]

print(HOST)

def update_watchdog(timestamp, cpu_usage, mem_usage) :
    sql = 'update watchdog set timestamp=' + str(timestamp) + ', cpu_usage=' + str(cpu_usage) + ', mem_usage=' + str(mem_usage) + ' where ip_addr="' + str(HOST) + '"'
    cursor.execute(sql)
    sieve_db.commit()

while True :
    # pid = os.getpid()
    # py  = psutil.Process(pid)
    # cpu_usage   = os.popen("ps aux | grep " + str(pid) + " | grep -v grep | awk '{print $3}'").read()
    # cpu_usage   = float(cpu_usage.replace("\n",""))
    # mem_usage  = round(py.memory_info()[0] /2.**30, 2)

    cpu_usage = psutil.cpu_percent(interval=0.1, percpu=False)
    mem_usage = psutil.virtual_memory().percent
    timestamp = time.time()
    timestamp = int(timestamp)

    update_watchdog(timestamp, cpu_usage, mem_usage)

    time.sleep(1)