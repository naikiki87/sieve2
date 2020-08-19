from socket import *
import pandas as pd
import pickle
import sys
import pymysql
HOST = ''
PORT = int(sys.argv[2])

BUFSIZE = 1024
ADDR = (HOST, PORT)
db_ip = '54.180.8.184'
db_name = 'nextk_202006'
db_pwd = 'dnjstjr1234'
db_id = 'root'

connection = pymysql.connect(host = db_ip, port= 3306, db= db_name, user= db_id, password = db_pwd)

def insert(connection, sql):
    cur = connection.cursor()
    cur.execute(sql)
    connection.commit()

serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(ADDR)
serverSocket.listen(100)
print('listen : ', PORT)
clientSocket, addr_info = serverSocket.accept()
print('connected')
res_df = pd.DataFrame(columns=['timestamp', 'size', 'density'])
res_df2 = pd.DataFrame(columns=['timestamp', 'avg_size', 'avg_density', 'std_size', 'std_density'])

unit = 5
first = 1

while True :
    data = clientSocket.recv(65535)
    data = data.decode()        ## bytes -> string
    if data != "" :
        data = data.split('\r\n')
        row = data[0].split('\t')
        # print("row : ", row)

        if first == 1 :
            first = 0
            temp = int(row[0])
            res_df.loc[len(res_df)] = [row[0], float(row[1]), float(row[2])]
        
        else :
            temp2 = int(row[0])
            gap = temp2 - temp
            
            res_df.loc[len(res_df)] = [row[0], float(row[1]), float(row[2])]

            if gap == 4 :
                avg_size = res_df['size'].mean()
                avg_density = res_df['density'].mean()
                std_size = res_df['size'].std()
                std_density = res_df['density'].std()

                res_df2.loc[len(res_df2)] = [temp, avg_size, avg_density, std_size, std_density]
             
                sql = 'insert into profile_nk values ('+str(int(res_df2[0:1].timestamp.values[0]))+' , '+str(res_df2[0:1].avg_size.values[0])+' , '+str(res_df2[0:1].std_size.values[0])+' , '+str(res_df2[0:1].avg_density.values[0])+' , ' + str(res_df2[0:1].std_density.values[0])+ ');'

                print("df : ", res_df2)
                print("sql : ", sql)
             
                insert(connection,sql)
                res_df = pd.DataFrame(columns=['timestamp', 'size', 'density'])
                res_df2 = pd.DataFrame(columns=['timestamp', 'avg_size', 'avg_density', 'std_size', 'std_density'])

                first = 1