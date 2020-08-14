const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
const args = process.argv;
var net = require('net')
var mysql = require('mysql')
var fpgrowth = require('node-fpgrowth')
var flag = 0;
var temp;
var tid = 0;
var server_port = args[2];
var port = args[2];

function run(server_port) {
  console.log("Server listening : " + server_port);
	net.createServer(async function(sock) {
		console.log("connected");
    var connection = net.createConnection(port, ip_address);
    console.log("connection complete!")
    console.log("Next Port : " + port);
		sock.on('data', function(data) {
      try {
        var data_str = data.toString();
        let data_row = data_str.split('\n');
        var item = data_row[0].split('\t');
        var item1 = Number(item[0]);
        var item2 = Number(item[2]);
        if(item1 > item2) {
          console.log(data_row[0]);
          connection.write(data + '\n');
        }
        else {}
      }
      catch(e) { console.log(e) };
		});
	}).listen(server_port);
}
run(server_port);
