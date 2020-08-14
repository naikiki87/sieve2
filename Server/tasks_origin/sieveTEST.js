const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
var net = require('net')
var mysql = require('mysql')
var fpgrowth = require('node-fpgrowth')
var flag = 0;
var temp;
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

async function run(server_port) {
  console.log("Server listening : " + server_port);
	net.createServer(async function(sock) {
		sock.on("error", function(err) {

			}
		)
		console.log("connected")
		sock.on('data', async function(data) {
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
		});

	}).listen(server_port);
}
// run(5125);
run(my_task_listening_port);
