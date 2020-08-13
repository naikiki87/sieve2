const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
var net = require('net')
var mysql = require('mysql')
var fpgrowth = require('node-fpgrowth')
var flag = 0;
var temp;

async function run(server_port) {
  console.log("Server listening : " + server_port);
	net.createServer(async function(sock) {
		sock.on("error", function(err) {

			}
		)
		console.log("connected")
		sock.on('data', async function(data) {
      // console.log("data : " + data)
      // console.log(typeof(data))
      let data_str = data.toString('utf8');
      let data_row = data_str.split('\n');
      // console.log(data_row)
      if(flag == 0) {
        temp = data_row[0];
      }
      if(flag == 1) {
        console.log("DATA");
        data_row.unshift(temp);
        data_row.pop();
        var time = `${Math.round(Date.now()/1000)}`;
        // console.log("Time : " + time);
        for(var i=0; i<data_row.length; i++) {
          var item = data_row[i].split('\t');
          // console.log(item[0] + " : " + item[1]);

        }
        // console.log(data_row);
      }
      if(flag == 0) { flag = 1; }
      else if(flag == 1) { flag = 0}
		});

	}).listen(server_port);
}
run(5125);
// run(my_task_listening_port);
