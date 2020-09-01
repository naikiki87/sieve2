from socket import *
import pandas as pd
import pickle
import sys
import sqlite3
import time

HOST = ''
PORT = int(sys.argv[2])
HOST_NEXT = sys.argv[3]
PORT_NEXT = int(sys.argv[4])
INPUT_SCHEMA = int(sys.argv[5])
U_NAME = sys.argv[6]
SCHE = sys.argv[7].split('/')
AGG_TYPE = int(sys.argv[8])
AGG_UNIT = int(sys.argv[9])
PANE = int(sys.argv[10])
SLD_SIZE = int(sys.argv[11])
QUERY = sys.argv[12]

DB = "./db/temp.db"
BUFSIZE = 1024
ADDR = (HOST, PORT)

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(ADDR)
serverSocket.listen(100)
print('listen : ', PORT)
clientSocket, addr_info = serverSocket.accept()
print('connected')

# client_next = socket(AF_INET, SOCK_STREAM)
# client_next.connect((HOST_NEXT, PORT_NEXT))

def create_Table(con) :
    cur = con.cursor()
    struct = ''
    for i in range(len(SCHE)) :
        temp = SCHE[i].split(',')
        s_name = temp[0]
        s_type = temp[1]
        if i == (len(SCHE) - 1) :
            struct = struct + s_name + ' ' + s_type
        else :
            struct = struct + s_name + ' ' + s_type + ', '

    sql = "create table if not exists " + U_NAME + ' (' + struct + ')'
    cur.execute(sql)

def insert_data(con, data) :
    cur = con.cursor()
    insert = ''
    for i in range(len(data)) :
        if i == (len(data)-1) :
            insert = insert + data[i]
        else :
            insert = insert + data[i] + ', '
    
    sql = "insert into " + U_NAME + " values (" + insert + ")"
    cur.execute(sql)

def delete_all(con) :
    cur = con.cursor()
    cur.execute("delete from " + U_NAME)

con = sqlite3.connect(':memory:')
cur = con.cursor()
create_Table(con)

first = 1
start_time = 0

while True :
    data = clientSocket.recv(65535)
    if data != "" :
        data = pickle.loads(data)
        insert_data(con, data)
        row_cnt = len(cur.execute("select * from " + U_NAME).fetchall())
        print("cnt : ", row_cnt)
        if AGG_TYPE == 0 :      # tuple 단위 집계
            if row_cnt % AGG_UNIT == 0 :
                res = cur.execute(QUERY)
                for row in res :
                    print(row)
                
                delete_all(con)

        elif AGG_TYPE == 1 :     # time 단위 집계
            print("time : ", time.time())
            if first == 1 :
                first = 0
                start_time = time.time()
                
            else :
                if (time.time() - start_time) > AGG_UNIT :
                    res = cur.execute(QUERY)
                    for row in res :
                        print(row)

                    delete_all(con)
                    first = 1

        # try :
        #     send = pickle.dumps(data)
        #     client_next.sendall(send)
        # except :
        #     client_next.close()