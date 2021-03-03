from socket import *
import pandas as pd
import pickle
import sys
import time
from time import sleep
import pymysql
import threading

HOST = sys.argv[1]
PORT = int(sys.argv[2])
HOST_NEXT = sys.argv[3]
PORT_NEXT = int(sys.argv[4])
IN_SCHEMA = int(sys.argv[5])
COL = int(sys.argv[6])

BUFSIZE = 1024
ADDR = (HOST, PORT)

def db_con() :
    conn = pymysql.connect(
        user='root', 
        passwd='Elql27!^', 
        host='3.34.91.188', 
        db='cell_test2', 
        charset='utf8'
    )
    return conn

# conn = db_con()
# cursor = conn.cursor(pymysql.cursors.DictCursor)
# # sql = 'select id from engine_computer where ip_address="' + HOST + '"'
# sql = 'select id from engine_computer where ip_address="165.132.105.40"'
# cursor.execute(sql)
# result = cursor.fetchall()
# ec_id = result[0]['id']
# conn.close()

split_cnt = 1

next_client = []

def monitoring_split() :
    print("monitoring start")
    while True :
        try :
            conn = db_con()
            cursor = conn.cursor(pymysql.cursors.DictCursor)
            sql = 'SELECT split FROM job_tasks where ec_id=' + str(ec_id) + ' and listening_port=' + str(PORT)
            cursor.execute(sql)
            result = cursor.fetchall()
            split = result[0]['split']
            conn.close()

            if split == 1 :
                print("split : ", split)
                receive_append()
                break
        except :
            pass
        time.sleep(2)

# split_mon = threading.Thread(target=monitoring_split)
# split_mon.start()

def receive_append() :
    global split_cnt, next_client
    print("receive_append : ", split_cnt)

    conn = db_con()
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    # sql = 'SELECT sec_dest_ip, sec_dest_port FROM job_tasks where ec_id=' + str(ec_id) + ' and listening_port=40001'
    sql = 'SELECT sec_dest_ip, sec_dest_port FROM job_tasks where ec_id=' + str(ec_id) + ' and listening_port=' + str(PORT)
    cursor.execute(sql)
    result = cursor.fetchall()
    conn.close()
    next_host = result[0]['sec_dest_ip']
    next_port = result[0]['sec_dest_port']

    print("next host : ", next_host, next_port, split_cnt)
    temp = socket(AF_INET, SOCK_STREAM)
    temp.connect((next_host, next_port))
    next_client.append(temp)
    split_cnt = split_cnt + 1
    print("split cnt : ", split_cnt)

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(ADDR)
serverSocket.listen(100)
print('listen : ', PORT)
clientSocket, addr_info = serverSocket.accept()
print('connected')
temp = socket(AF_INET, SOCK_STREAM)
temp.connect((HOST_NEXT, PORT_NEXT))
next_client.append(temp)

while True :
    data = clientSocket.recv(65535)
    if data != "" :
        data = pickle.loads(data)

    try :
        tempdata = data.split('\t')
        send_data = pickle.dumps(tempdata[COL])

        for j in range(split_cnt) :
            next_client[j].sendall(send_data)
    except :
        pass