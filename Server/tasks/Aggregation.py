from socket import *
import pandas as pd
import pickle
import sys
import sqlite3
import json

DB = "./db/temp.db"
U_NAME = sys.argv[1]
TEST = sys.argv[2]
SCHE = TEST.split('/')

def db_SetTable() :
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
    print("sql : ", sql)

    conn = sqlite3.connect(DB)
    cur = conn.cursor()
    # sql = "create table if not exists STATUS (code text, step integer, ordered integer, orderType integer, trAmount integer)"
    cur.execute(sql)
    conn.commit()
    conn.close()

def db_INSERT(code, step, ordered, orderType, trAmount):
    conn = sqlite3.connect(DB)
    cur = conn.cursor()
    sql = "insert into STATUS (code, step, ordered, orderType, trAmount) values(:CODE, :STEP, :ORDERED, :ORDERTYPE, :TRAMOUNT)"
    cur.execute(sql, {"CODE" : code, "STEP" : step, "ORDERED" : ordered, "ORDERTYPE" : orderType, "TRAMOUNT" : trAmount})
    conn.commit()
    conn.close()
    print(now, "[MAIN]", "data INSERTED")

def db_DELETE(code):
    conn = sqlite3.connect(DB)
    cur = conn.cursor()
    sql = "delete from STATUS where code = :CODE"
    cur.execute(sql, {"CODE" : code})
    conn.commit()
    conn.close()
    print(now, "[MAIN]", "data DELETED")

db_SetTable()

# HOST = ''
# PORT = int(sys.argv[2])
# HOST_NEXT = sys.argv[3]
# PORT_NEXT = int(sys.argv[4])
# INPUT_SCHEMA = int(sys.argv[5])

# BUFSIZE = 1024
# ADDR = (HOST, PORT)

# serverSocket = socket(AF_INET, SOCK_STREAM)
# serverSocket.bind(ADDR)
# serverSocket.listen(100)
# print('listen : ', PORT)
# clientSocket, addr_info = serverSocket.accept()
# print('connected')

# client_next = socket(AF_INET, SOCK_STREAM)
# client_next.connect((HOST_NEXT, PORT_NEXT))

# while True :
#     data = clientSocket.recv(65535)
#     if data != "" :
#         data = pickle.loads(data)
#         print(type(data))
#         print("receive : ", data)

        # try :
        #     send = pickle.dumps(data)
        #     client_next.sendall(send)
        # except :
        #     client_next.close()