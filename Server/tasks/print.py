import sys
import pymysql
import socket
import json
args = sys.argv
my_job_id = args[1];
my_task_id = args[2];
my_task_ip = args[3];
my_task_listening_port = args[4];
db_ip = args[5];
db_name = args[6];
db_id = args[7];
db_pwd = args[8];
output_type = int(args[9]);

print(args)

global_config = {}
f_input_schema = []
f_output_schema = []

connection = pymysql.connect(host=db_ip, port=3306, db=db_name, user=db_id, password=db_pwd)

def insert(connection, sql):
	cur = connection.cursor()
	cur.execute(sql)
	connection.commit()
def do_query(connection, sql):
	cur = connection.cursor()
	cur.execute(sql)
	rows = cur.fetchall()
	return rows

def send_socket(host,port,data):
	port = int(port)
	soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)	 
	soc.connect((host, port))
	soc.send(data.encode())
	soc.close()
	
# server
def rec_data(conn, MAX_BUFFER_SIZE):  
	input_from_client_bytes = conn.recv(MAX_BUFFER_SIZE)

	import sys
	siz = sys.getsizeof(input_from_client_bytes)
	if	siz >= MAX_BUFFER_SIZE:
		print("The length of input is probably too long: {}".format(siz))

	input_from_client = input_from_client_bytes.decode("utf8").rstrip()

	return input_from_client

def client_thread(conn, ip, port,data_callback, MAX_BUFFER_SIZE = 88888):

	# read lines periodically without ending connection
	still_listen = True
	while still_listen:
		input_from_client = rec_data(conn, MAX_BUFFER_SIZE)

		# if you receive this, end the connection
		if "--ENDOFDATA--" in input_from_client:
			print('--ENDOFDATA--')
			conn.close()
			print('Connection ' + ip + ':' + port + " ended")
			still_listen = False
		else:			 
			splin = input_from_client.split('\t')
			data_callback(splin)
			#print("{}, {}".format(splin[0], splin[1]))

			# tell client that we can accept another data processing
			#conn.sendall("-".encode("utf8"))


def start_server(port, data_callback):

	soc = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	# this is for easy starting/killing the app
	soc.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	print('Socket created')

	try:
		soc.bind(("", port))
		print('Socket bind complete')
	except socket.error as msg:

		print('Bind failed. Error : ' + str(sys.exc_info()))
		sys.exit()

	#Start listening on socket
	soc.listen(10)
	print('Socket now listening')

	# for handling task in separate jobs we need threading
	from threading import Thread

	# this will make an infinite loop needed for 
	# not reseting server for every client
	while True:
		conn, addr = soc.accept()
		ip, port = str(addr[0]), str(addr[1])
		print('Accepting connection from ' + ip + ':' + port)
		try:
			Thread(target=client_thread, args=(conn, ip, port, data_callback)).start()
		except:
			print("Terible error!")
			import traceback
			traceback.print_exc()
	soc.close()

			
import time, threading

def setInterval(interval, times = -1):
	# This will be the actual decorator,
	# with fixed interval and times parameter
	def outer_wrap(function):
		# This will be the function to be
		# called
		def wrap(*args, **kwargs):
			# This is another function to be executed
			# in a different thread to simulate setInterval
			def inner_wrap():
				i = 0
				while i != times:
					time.sleep(interval)
					function(*args, **kwargs)
					i += 1
			threading.Timer(0, inner_wrap).start()
		return wrap
	return outer_wrap

# interval, how many times?
@setInterval(20) 
def heartbeat(host, port,job_id, task_id):
	send_socket(host, port, job_id + "\t" + task_id)


def get_infos(connection, my_job_id, my_task_id, args): 
	# heartbeat temporary disabled
	'''h_sql = "select hjt.listening_port, ec.ip_address from job_tasks jt join job_tasks hjt on jt.heartbeat_task_id = hjt.task_id and jt.heartbeat_job_id = hjt.job_id join engine_computer ec on ec.id = hjt.ec_id	where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	h_info = do_query(connection, h_sql)
	hlt_port = h_info[0][0]
	hlt_ip = h_info[0][1]
	# do heartbeat code must be here
	heartbeat(hlt_ip, hlt_port, my_job_id, my_task_id)'''
	i_sql = "select c.name from cell_schemas s join cell_columns c on c.schema_id = s.id join job_tasks jt on jt.input_schema_id = s.id where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	input_schema = do_query(connection, i_sql)
	for i in input_schema:
		f_input_schema.append(i[0])
	o_sql = "select c.name from cell_schemas s join cell_columns c on c.schema_id = s.id join job_tasks jt on jt.output_schema_id = s.id where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	output_schema = do_query(connection, o_sql)
	for i in output_schema:
		f_output_schema.append(i[0])
		
	c_sql = "select config from job_tasks jt where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	
	tt = do_query(connection, c_sql)
	t_config = json.loads(tt[0][0])
	if output_type == 0 :  # socket output
		output_ip = args[10];
		output_port = args[11];
		t_config['output_ip'] = output_ip
		t_config['output_port'] = output_port
				
	elif output_type == 1 : # file output
		output_file_name = args[10];
		t_config['output_file_name'] = output_file_name
	elif output_type == 2 : #	db output
		output_db_ip = args[10];
		output_db_name = args[11];
		output_db_id = args[12];
		output_db_pwd = args[13];
		output_db_table_name = args[14];
		
		t_config['output_db_ip'] = output_db_ip
		t_config['output_db_name'] = output_db_name
		t_config['output_db_id'] = output_db_id
		t_config['output_db_pwd'] = output_db_pwd
		t_config['output_db_table_name'] = output_db_table_name
		output_connection = pymysql.connect(host=output_db_ip, port=3306, db=output_db_name, user=output_db_id, password=output_db_pwd)
		t_config['output_connection'] = output_connection
		
	
	elif output_type ==3 :	# heartbeat
		pass
	else :
		 print("Output type not defined");
		 return;
	
	global_config = t_config;
	return t_config
	
# data must be one row string. column delimeter must be ,
def output(output_type, config, output_schema, data):

	data_with_delimeter = "\t".join(data.split(","))
	if output_type == 0: #socket output
		output_ip = config['output_ip'];
		output_port = config['output_port'];
		try :
			print(output_ip, output_port, data_with_delimeter)
			send_socket(output_ip, output_port, data_with_delimeter)
		except :
			print("error!")
	elif output_type == 1: #file output
		output_file_name = config['output_file_name'];
		try :
			with open(output_file_name, 'a') as out:
				out.write(data_with_delimeter + '\n')
		except :
			print("error!")
	elif output_type == 2 : #db output
		output_db_ip = config['output_db_ip']
		output_db_name = config['output_db_name']
		output_db_id = config['output_db_id']
		output_db_pwd = config['output_db_pwd']
		output_db_table_name = config['output_db_table_name']
		dd = data.split(",");
		for i, ii in enumerate(dd):
			dd[i] = "'" + dd[i] + "'"
		sql = "insert into " + output_db_table_name + " ( " + ",".join(output_schema) + " ) values (" +	 ",".join(dd) + ")";
		insert(config['output_connection'], sql)
		
	
	elif output_type == 3 : # heartbeat
		pass
	else :
		print("Output type not defined");
		return;

#your algorithm must be here
def data_callback(data_arr):
	print(data_arr)
	output(output_type, global_config, f_output_schema, ",".join(data_arr))
	
t_config = get_infos(connection, my_job_id, my_task_id, args)
global_config = t_config;
print(global_config)
start_server(int(my_task_listening_port), data_callback)	