import socket, threading

class ClientThread(threading.Thread):

    def __init__(self,ip,port):
        threading.Thread.__init__(self)
        self.ip = ip
        self.port = port


    def run(self):    
        # clientsock.send("\nWelcome to the server\n\n")

        data = "dummydata"

        while len(data):
            data = clientsock.recv(2048)
            print(data)
            # clientsock.send("You sent me : "+data)

        # print "Client disconnected..."

host = "165.132.145.57"
port = 40005

tcpsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
tcpsock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

tcpsock.bind((host,port))
threads = []


while True:
    tcpsock.listen(4)
    print("listen : ", port)
    # print "\nListening for incoming connections..."
    (clientsock, (ip, port)) = tcpsock.accept()
    newthread = ClientThread(ip, port)
    newthread.start()
    threads.append(newthread)

for t in threads:
    t.join()