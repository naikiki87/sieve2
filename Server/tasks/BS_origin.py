from socket import *
import pandas as pd
import pickle
import sys
import time

HOST = ''
PORT = int(sys.argv[2])
HOST_NEXT = sys.argv[3]
PORT_NEXT = int(sys.argv[4])
INPUT_SCHEMA = int(sys.argv[5])
# INTERVAL = int(sys.argv[5])

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

df = pd.DataFrame(columns = ['a1', 'a2', 'a3'])

next_client = socket(AF_INET, SOCK_STREAM)
next_client.connect((HOST_NEXT, PORT_NEXT))

file = open('2d_int_int.txt', 'r')
s = file.read()
data = s.split('\n')

i = 0

df.loc[len(df)] = [1, 2, 3]
df.loc[len(df)] = [1, 3, 3]
df.loc[len(df)] = [2, 4, 5]
df.loc[len(df)] = [3, 6, 9]
print(df)

while True :
    try :
        send_data = pickle.dumps(df)
        next_client.sendall(send_data)
        time.sleep(INTERVAL)
    except :
        next_client.close()


    # if i == len(data) :
    #     i = 0
    # row = data[i].split('\t')

    # try :
    #     send_data = pickle.dumps(row)
    #     next_client.sendall(send_data)
    # except :
    #     next_client.close()

    # time.sleep(INTERVAL)
    # i = i + 1