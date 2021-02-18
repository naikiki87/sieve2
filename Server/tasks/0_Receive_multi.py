from socket import *
import logging
# import pandas as pd
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

HOST = sys.argv[1]
PORT = int(sys.argv[2])

BUFSIZE = 1024
ADDR = (HOST, PORT)

socket1 = socket(AF_INET, SOCK_STREAM)
socket1.bind(ADDR)
socket1.listen(100)

# socket2.listen(100)

def Recv(conn): 
    while conn :
        data = conn.recv(1024)
        print(data)
        
    # print('Thread Recv' + str(count) + ' Start') 
    # while True: 
    #     data = conn.recv(1024).decode() 
    #     print(data)
        # send_queue.put([data, conn, count]) #각각의 클라이언트의 메시지, 소켓정보, 쓰레드 번호를 send로 보냄


print('listen : ', PORT)

group = []

while True :
    conn, addr = socket1.accept()
    print('connected : ', addr)

    thread1 = threading.Thread(target = Recv, args=(conn))
    # thread1.start()
    # thread.start_new_thread(Recv(conn))



# while True :
#     data = clientSocket1.recv(65535)
#     if data != "":
#         data = pickle.loads(data)
#         print("RCV DATA : ", data)
#         logger.info(data)