from socket import *
import logging
# import pandas as pd
import time
import pickle
import sys
import threading

logger = logging.getLogger()
logger.setLevel(logging.INFO)
formatter = logging.Formatter('%(message)s')

f = open("logs.txt", "w")
f.close()

file_handler = logging.FileHandler('logs.txt')
file_handler.setFormatter(formatter)
logger.addHandler(file_handler)

# HOST = sys.argv[1]
# PORT = int(sys.argv[2])
HOST = "165.132.145.57"
PORT = 40001

BUFSIZE = 1024
ADDR = (HOST, PORT)

soc = socket(AF_INET, SOCK_STREAM)
soc.bind(ADDR)
soc.listen(100)

print('listen : ', PORT)

queue_a = []
queue_b = []

def Recv(conn, addr, count) :
    while True :
        data = conn.recv(65535)
        data = pickle.loads(data)
        print(addr[0], ' : ', data)

        if count == 1 :
            queue_a.append(data)
        elif count == 2 :
            queue_b.append(data)

count = 0
show = 0

while True :
    if count == 2 :         # 2개 connection을 받음
        break
    else :
        count = count + 1
        conn, addr = soc.accept()
        print('connected : ', count)
        t = threading.Thread(target=Recv, args=(conn, addr, count))
        t.start()

while True :
    if show == 3 :
        show = 0
        print("A : ", queue_a)
        print("B : ", queue_b)
    else :
        show = show + 1
    time.sleep(1)