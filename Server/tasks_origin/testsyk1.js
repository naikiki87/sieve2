const express = require('express');
const http = require('http');
const fs = require('fs');
const app = express();
var net = require('net')
var mysql = require('mysql')
var fpgrowth = require('node-fpgrowth');
const e = require('express');
var flag = 0
var receive = []

async function run(server_port) {
  console.log("Server listening : " + server_port);
	net.createServer(async function(sock) {
		sock.on("error", function(err) {
			}
		)
		console.log("connected")
		sock.on('data', async function(data) {    // 하나의 result 가 2개로 쪼개져 들어옴 -> 왜 그런진 나도 몰라 시발
      let data_str = data.toString('utf8');   // string 형태의 데이터 receive
      let data_row = data_str.split('\n');    // \n 을 기준으로 데이터 잘라냄 -> data_row
      if(flag == 0) {
        for(var i=0; i<data_row.length - 1; i++ ) {
          receive.push(data_row[i])
        }
        flag = 1
      }
      else if(flag == 1) {
        for(var i=0; i<data_row.length - 1; i++ ) {
          receive.push(data_row[i])
        }
        flag = 0
        console.log("receive : " + receive)
        console.log("a1 : ", receive[0])
        console.log(receive[0].typeof)

        let item0 = receive[0].split('\t')
        console.log("item0 : ", item0)
        receive = []
      }

      // let data_item = data_str.split('\t');   // tab을 기준으로 데이터를 잘라서 list화
      // var len_data = data_item.length
      // data_item[len_data-1] = data_item[len_data - 1].replace('\n', '')   // 마지막 데이터에 \n을 없앰
      // var time = `${Math.round(Date.now()/1000)}`;
      // console.log("[DATA] " + time + ' : ' + data_item)
      // console.log(data_item)
		});

	}).listen(server_port);
}
run(5127);