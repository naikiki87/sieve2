from socket import *
import pandas as pd
import pickle
import sys

HOST = ''
PORT = int(sys.argv[2])

BUFSIZE = 1024
ADDR = (HOST, PORT)

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(ADDR)
serverSocket.listen(100)
print('listen : ', PORT)
clientSocket, addr_info = serverSocket.accept()
print('connected')

while True :
    data = clientSocket.recv(65535)
    # data = data.decode()        ## bytes -> string
    if data != "" :
        # print("data : ", data)
        data = pickle.loads(data)
        print("before : ", data)

        # send_data = pickle.dumps(data)

        # client_next.sendall(send_data)
        # data2 = pickle.loads(send_data)
        # print("after : ", data2)
    #     print(type(data))
        # send_data = pickle.dumps(data)

        # try :
        #     print("send : ", send_data)
    #         client_next.sendall(send_data)
        
    #     except :
    #         client_next.close()