const mysql = require('mysql');
const net = require('net');
const args = process.argv;

const my_job_id = args[2];
const my_task_id = args[3];
const my_task_ip = args[4];
const my_task_listening_port = args[5];
const db_ip = args[6];
const db_name = args[7];
const db_id = args[8];
const db_pwd = args[9];
const output_type = args[10];

if(output_type == 0) { // socket output
	var output_task_id = args[11];
    		
}
else if(output_type == 1) { // file output
	
	var output_file_name = args[11];
}
else if(output_type == 2) { // db output
	var output_db_ip = args[11];
	var output_db_name = args[12];
	var output_db_id = args[13];
	var output_db_pwd = args[14];
	var output_db_table_name = args[15];
	
}
else if (output_type ==3) { // heartbeat
	
}
else {
	 console.log("Output type not defined");
	 return;
}


const connection = mysql.createPool({
    connectionLimit: 20,
    host: db_ip,
    user: db_id,
	port: 3306,
    password: db_pwd,
    database: db_name,
  });


doQuery = (sql) => {
  return new Promise( (resolve, reject) => {
    connection.getConnection((err, connection) => {
      if(err) return reject(err);
      connection.query(sql, (err, rows) => {
        connection.release();
        if (err) return reject(err);
        resolve(rows);
      });
    });
  });
};


make_server();


function make_server()
{
	var listening_port = my_task_listening_port;
	var server = net.createServer(async function(sock) 
	{
		//console.log("heartbeat_controller running in ", listening_port)
		///sock.setNoDelay(true);
		sock.on("error", function(err) 
		{
			console.log(err)
			server.close();
			setTimeout(function(){make_server()}, 4000)
		});
		sock.on('data', async function(data) 
		{
			try {
			let data_str = data.toString('utf8');
			data_str = data_str.replace('\r\n','');
			let data_arr = data_str.split('\t')
				console.log(data_arr)
				let val_str = "'" + data_arr[0] + "'" + ',' + "'" + data_arr[1]+ "'";
				await doQuery("INSERT INTO watchdog_heartbeat (job_id, task_id) VALUES("+val_str+") ON DUPLICATE KEY UPDATE last_timestamp = now()");
			} catch (err) {
					console.log(err);
				}
		})
		
	}).listen(listening_port, function()
	{
		console.log('server start in ', listening_port)
	});

	server.on('error', function (e) 
	{
		if (e.code == 'EADDRINUSE') 
		{
			console.log('Address in use, retrying...');
			setTimeout(function () 
			{
				server.close();
				make_server();
			}, 3000);
		}
		else  // when heart beat client die
		{
			console.log("server error", e)
			
		}

	})
}

async function check_error()
{
		var errors = await doQuery("select * from watchdog_heartbeat wh where (now() - wh.last_timestamp) > 180");
		//console.log(errors)
		for (var i = 0 ; i < errors.length; i++) { // UI의 실행 router에 재실행 요청 전송해야함.
			
		}
		 
		
}

check_error();
setInterval(async function(){
	await doQuery("flush hosts");
	check_error();
	}, 120000);