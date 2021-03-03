from socket import *
import logging
# import pandas as pd
import pickle
import sys
import queue
import time

logger = logging.getLogger()
logger.setLevel(logging.INFO)
formatter = logging.Formatter('%(message)s')

f = open("logs.txt", "w")
f.close()

file_handler = logging.FileHandler('logs.txt')
file_handler.setFormatter(formatter)
logger.addHandler(file_handler)

HOST = sys.argv[1]
PORT = int(sys.argv[2])
# HOST = "165.132.145.57"
# PORT = 40001

print("INFO : ", HOST, PORT)

BUFSIZE = 1024
ADDR = (HOST, PORT)

q = queue.Queue()

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(ADDR)
serverSocket.listen(100)

clientSocket, addr_info = serverSocket.accept()
print('listen : ', PORT)

b_time = time.time()

while True :
    data = clientSocket.recv(65535)
    if data != "":
        data = pickle.loads(data)
        print(data)
        # if int(data) % 100 == 0 :
        #     print("RCV DATA : ", data)
        #     print(time.time() - b_time)
        # q.put(data)
        # print("size : ", q.qsize())

        # logger.info(data)