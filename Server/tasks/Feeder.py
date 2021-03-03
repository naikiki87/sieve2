from socket import *
import pandas as pd
import pickle
import sys
import time
from time import sleep

HOST_NEXT = sys.argv[1]
PORT_NEXT = int(sys.argv[2])
INTERVAL = int(sys.argv[3])

interval = 1/INTERVAL

next_client = socket(AF_INET, SOCK_STREAM)
next_client.connect((HOST_NEXT, PORT_NEXT))


file = open('2d_int_int.txt', 'r')
s = file.read()
data = s.split('\n')

i = 0
# interval = 0.5
cnt = 0
iid = 0

while True :
    if i == len(data) :
        i = 0
        # cnt = cnt + 1

        # if cnt < 2 :
        #     interval = 0.5
        
        # else :
        #     interval = 0.05
        #     if cnt == 10 :
        #         cnt = 0
        
    # row = str(iid) + ' : ' + str(data[i].split('\t'))

    row = data[i]
    # row = str(iid)
    # iid = iid + 1

    try :
        # if iid == 10 :
        #     interval = 0.1
        
        # if iid == 50 :
        #     interval = 0.01

        if iid == 101 :
            break
        # elif iid % 100 == 0 :
            # print("send : ", iid)
        else :
            print("send : ", row)

            send_data = pickle.dumps(row)
            next_client.sendall(send_data)
    except :
        next_client.close()

    sleep(interval)
    i = i + 1