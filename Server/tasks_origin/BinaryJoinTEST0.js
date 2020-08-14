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
      var item1 = Number(item[1]);
      var item2 = Number(item[2]);
      if(item1 > item2) {
        console.log("1 : " + item[0] + '/' + item[1] + '/' + item[2]);
      }
      else {
        console.log("2 : " + item[0] + '/' + item[1] + '/' + item[2]);
      }
		});
	}).listen(server_port);
}
run(port);
