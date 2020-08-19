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
res_df = pd.DataFrame(columns={'timestamp', 'size', 'density'})
print("df : ", res_df)

while True :
    data = clientSocket.recv(65535)
    data = data.decode()        ## bytes -> string
    if data != "" :
        print("data : ", data)
        # data = data.split('\r\n')
        # row = data[0].split('\t')
        # print("data : ", row)


        