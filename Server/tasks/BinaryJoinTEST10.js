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
var ip_address = "165.132.105.40";
var server_port = args[2];
var port = args[3];

function run(server_port) {
  console.log("Server listening : " + server_port);
  console.log("Next port : " + port);
	net.createServer(async function(sock) {
		console.log("connected");
    var connection = net.createConnection(port, ip_address);
    console.log("connection complete!")
		sock.on('data', function(data) {
      try
      {
        var data_str = data.toString();
        let data_row = data_str.split('\n');
        let str = data_row[0];
        console.log(str);
        var data = str+'\n';
        connection.write(data);
      }
      catch (e) {console.log(e)}
		});
	}).listen(server_port);
}
run(server_port);
