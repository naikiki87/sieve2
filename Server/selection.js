var ver = 1.0;
const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
var net = require('net')
var mysql = require('mysql')
var fpgrowth = require('node-fpgrowth')

async function run(server_port) {
  console.log("Server listening : " + server_port);
	net.createServer(async function(sock) {
		sock.on("error", function(err) {

			}
		)
		console.log("connected")
		//
		sock.on('data', async function(data) {
			let data_str = data.toString('utf8');
			let data_row = data_str.split('\n');
			console.log(data_row)
		});

	}).listen(server_port);
}
run(5123);
