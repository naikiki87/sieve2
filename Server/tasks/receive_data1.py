from socket import *
import pandas as pd
import pickle
import sys

HOST = ''
PORT = int(sys.argv[2])
HOST_NEXT = sys.argv[3]
PORT_NEXT = int(sys.argv[4])

BUFSIZE = 1024
ADDR = (HOST, PORT)

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(ADDR)
serverSocket.listen(100)
print('listen : ', PORT)
clientSocket, addr_info = serverSocket.accept()
print('connected')

client_next = socket(AF_INET, SOCK_STREAM)
client_next.connect((HOST_NEXT, PORT_NEXT))

while True :
    data = clientSocket.recv(65535)
    # data = data.decode()        ## bytes -> string
    if data != "" :
        # print("data : ", data)
        data = pickle.loads(data)
        print("before : ", data)

        try :
            send = pickle.dumps(data)
            client_next.sendall(send)
        except :
            client_next.close()