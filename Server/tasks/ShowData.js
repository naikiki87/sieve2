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
var port = args[2];

function run(server_port) {
  console.log("Server listening : " + server_port);
	net.createServer(async function(sock) {
		console.log("connected");
		sock.on('data', function(data) {
      var data_str = data.toString();
      let data_row = data_str.split('\n');
      var item = data_row[0].split('\t');
      console.log(data_row[0]);
		});
	}).listen(server_port);
}
run(port);
