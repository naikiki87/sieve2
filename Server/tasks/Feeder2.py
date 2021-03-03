from socket import *
import pandas as pd
import pickle
import sys
import time
from time import sleep
import pymysql
import threading

HOST_NEXT = sys.argv[1]
PORT_NEXT = int(sys.argv[2])
INTERVAL = int(sys.argv[3])

def db_con() :
    conn = pymysql.connect(
        user='root', 
        passwd='Elql27!^', 
        host='3.34.91.188', 
        db='cell_test2', 
        charset='utf8'
    )
    return conn

conn = db_con()
cursor = conn.cursor(pymysql.cursors.DictCursor)
# sql = 'SELECT id FROM engine_computer where ip_address="' + HOST + '"'
sql = 'select id from engine_computer where ip_address="165.132.105.40"'
cursor.execute(sql)
result = cursor.fetchall()
ec_id = result[0]['id']
conn.close()

interval = 1/INTERVAL

split_cnt = 1

next_client = []
temp = socket(AF_INET, SOCK_STREAM)
temp.connect((HOST_NEXT, PORT_NEXT))
next_client.append(temp)

def monitoring_split() :
    while True :
        conn = db_con()
        cursor = conn.cursor(pymysql.cursors.DictCursor)
        # sql = 'SELECT split FROM job_tasks where ec_id=' + str(ec_id) + ' and listening_port=' + str(PORT)
        sql = 'SELECT split FROM job_tasks where ec_id=' + str(ec_id) + ' and listening_port=40001'
        cursor.execute(sql)
        result = cursor.fetchall()
        split = result[0]['split']

        if split == 1 :
            print("split : ", split)
            receive_append()
            break
        time.sleep(1)

split_mon = threading.Thread(target=monitoring_split)
split_mon.start()

def receive_append() :
    global split_cnt, next_client

    conn = db_con()
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    sql = 'SELECT sec_dest_ip, sec_dest_port FROM job_tasks where ec_id=' + str(ec_id) + ' and listening_port=40001'
    cursor.execute(sql)
    result = cursor.fetchall()
    next_host = result[0]['sec_dest_ip']
    next_port = result[0]['sec_dest_port']

    print("next host : ", next_host, next_port)

    
    # HOST2 = "165.132.145.57"
    # PORT2 = 40002
    # print("receive append : ", HOST2, PORT2)

    # temp = socket(AF_INET, SOCK_STREAM)
    # temp.connect((HOST2, PORT2))
    # next_client.append(temp)

    # split_cnt = split_cnt + 1

file = open('2d_int_int.txt', 'r')
s = file.read()
data = s.split('\n')

i = 0
while True :
    if i == len(data) :
        i = 0
    row = data[i].split('\t')

    try :
        print("send : ", row)
        send_data = pickle.dumps(row)

        for j in range(split_cnt) :
            next_client[j].sendall(send_data)
    except :
        pass

    sleep(interval)
    i = i + 1