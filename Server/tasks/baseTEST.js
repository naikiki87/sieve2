const mysql = require('mysql');
const net = require('net');
const fs = require('fs');
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
const f_input_schema = []
const f_output_schema = []

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
doQuery_with_connection = (sql, connection) => {
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

async function get_infos()
{
	// get heartbeat info
	var h_sql = "select hjt.listening_port, ec.ip_address from job_tasks jt join job_tasks hjt on jt.heartbeat_task_id = hjt.task_id and jt.heartbeat_job_id = hjt.job_id join engine_computer ec on ec.id = hjt.ec_id  where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	var h_info = await doQuery(h_sql)
	var hlt_port = h_info[0].listening_port
	var hlt_ip = h_info[0].ip_address

	//do heartbeat
	setInterval(function(){
		var hlt_data = my_job_id + "\t" + my_task_id
		send_socket_data(hlt_port, hlt_ip, hlt_data);
	}, 20 * 1000);

	//get input , output schema and config
	var i_sql = "select c.name from cell_schemas s join cell_columns c on c.schema_id = s.id join job_tasks jt on jt.input_schema_id = s.id where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	const input_schema = await doQuery(i_sql);
	for ( var i = 0 ; i < input_schema.length; i ++)
	{
		f_input_schema.push(input_schema[i].name)
	}
	var o_sql = "select c.name from cell_schemas s join cell_columns c on c.schema_id = s.id join job_tasks jt on jt.output_schema_id = s.id where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;
	const output_schema = await doQuery(o_sql);

	for ( var i = 0 ; i < output_schema.length; i ++)
	{
		f_output_schema.push(output_schema[i].name)
	}
	var c_sql = "select * from job_tasks jt where jt.job_id = "+my_job_id+" and jt.task_id = " + my_task_id;

	//var t_config = {}
	tt = await doQuery(c_sql);
	t_config = JSON.parse(tt[0].config)
	if(output_type == 0) { // socket output
		var output_ip = args[11];
		var output_port = args[12];
		t_config['output_ip'] = output_ip
		t_config['output_port'] = output_port

	}
	else if(output_type == 1) { // file output

		var output_file_name = args[11];
		t_config['output_file_name'] = output_file_name
	}
	else if(output_type == 2) { // db output
		var output_db_ip = args[11];
		var output_db_name = args[12];
		var output_db_id = args[13];
		var output_db_pwd = args[14];
		var output_db_table_name = args[15];

		t_config['output_db_ip'] = output_db_ip
		t_config['output_db_name'] = output_db_name
		t_config['output_db_id'] = output_db_id
		t_config['output_db_pwd'] = output_db_pwd
		t_config['output_db_table_name'] = output_db_table_name
		const output_connection = mysql.createPool({
			connectionLimit: 20,
			host: output_db_ip,
			user: output_db_id,
			port: 3306,
			password: output_db_pwd,
			database: output_db_name,
		  });
		t_config['output_connection'] = output_connection

	}
	else if (output_type ==3) { // heartbeat

	}
	else {
		 console.log("Output type not defined");
		 return;
	}
	console.log(t_config)
	return t_config
}
async function main() {
	var config = await get_infos()
	console.log(config)
	make_server(config);
}
main()

// data must be one row string. column delimeter must be ,
async function output(output_type, config, output_schema, data)
{
	var data_with_delimeter = data.split(",").join("\t")
	if(output_type == 0)
	{ // socket output
		var output_ip = config.output_ip;
		var output_port = config.output_port;
		try {
			//console.log(data)
			 send_socket_data(output_port, output_ip, data_with_delimeter)
		} catch(e) {
			console.log(e)
		}
	}
	else if(output_type == 1)
	{ // file output
		var output_file_name = config.output_file_name;
		try {
				fs.appendFile(output_file_name, data_with_delimeter, function(err)
				{
					if(err)
					{
						return console.log(err);
					}
				});
		} catch (e) {
			console.log(e)
		}
	}
	else if(output_type == 2) { // db output
		var output_db_ip = config.output_db_ip
		var output_db_name = config.output_db_name
		var output_db_id = config.output_db_id
		var output_db_pwd = config.output_db_pwd
		var output_db_table_name = config.output_db_table_name
		var dd = data.split(",");
		for(var j = 0 ; j < dd.length; j++)
		{
			dd[j] = "'" + dd[j] + "'"
		}
		var t_sql = "insert into " + output_db_table_name + " ( " + output_schema.join(",") + " ) values (" +  dd.join(",") + ")";
		await doQuery_with_connection(t_sql,config.output_connection)

	}
	else if(output_type == 3) { // heartbeat

	}
	else {
		 console.log("Output type not defined");
		 return;
	}

}
function send_socket_data(port, host, data)
{
	//console.log(port, host,data)
	try	{
		var c = net.createConnection(port, host);
		c.on('connect', function()
		{
			try {
				c.write(data)
				c.destroy();
			} catch (e) {
			}
		}).on('error', function(e){
		});
	} catch (e) {
		//console.log(e)
		}

}
var flag = 0;

function make_server(config)
{
	var listening_port = my_task_listening_port;
	var server = net.createServer(async function(sock)
	{
		sock.on("error", function(err)
		{
			console.log(err)
			server.close();
			setTimeout(function(){make_server(config)}, 4000)
		});
		sock.on('data', async function(data)
		{
			try {
        let data_str = data.toString('utf8');
  			let data_row = data_str.split('\n');
        if(flag == 0) {
          temp = data_row[0];
        }
        if(flag == 1) {
          console.log("DATA");
          data_row.unshift(temp);
          data_row.pop();
          var time = `${Math.round(Date.now()/1000)}`;
          console.log("Time : " + time);
          for(var i=0; i<data_row.length; i++) {
            var item = data_row[i].split('\t');
            console.log(item[0] + " : " + item[1]);
          }
          // console.log(data_row);
        }
        if(flag == 0) { flag = 1; }
        else if(flag == 1) { flag = 0}


				// let data_str = data.toString('utf8');
				// data_str = data_str.replace('\r\n','');
				// let data_arr = data_str.split('\t')
				// output(output_type, config, f_output_schema, data_arr.join(","))
				/*
				Your Stream algorithm must be here.
				 */
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
				make_server(config);
			}, 3000);
		}
		else
		{
			console.log("server error", e)

		}

	})
}
