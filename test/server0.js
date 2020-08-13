const http = require('http');
const socketio = require('socket.io');
const fs = require('fs');
const express = require('express');
const app = express();

// static 폴더 경로 지정해준다. html 파일에서 src 요청할 때 매개변수로 지정해준 경로 이후부터 작성하면 된다.
app.use(express.static(__dirname + '/public'));

// http의 createServer에서 사용할 때 express를 매개변수로 넣어준다.
const server = http.createServer(app);

server.listen(5000, function(){
    console.log("Server running 5000");
});

// 연결할 http 서버 객체 매개변수로 사용해 소켓 연결
// src에서 이 객체를 사용한다.
const io = socketio.listen(server);

// on : 이벤트를 만들어라 connection은 예약어
io.sockets.on('connection', function(socket){

  console.log("브라우저 연결 : " + socket.id);

  // 접속한 브라우저 객체를 특정 공간에 등록.
  socket.join("chat");

  // broadcast라는 메소드 socket 통해 호출되면 매개변수로 전달된 data와 함께 적당한 로직 수행
  socket.on("broadcast", function (data) {
      // chat 공간에 등록된 브라우저들에 특정 이벤트 수행
    io.sockets.in("chat").emit('receive', data);
  });
});

// socket.io 사용시 Http 모듈로 Express를 실행한다.
app.use(function(request, response){
   fs.readFile("client.html", "utf-8", function(err, data){
       response.type('text/html');
       response.send(data);
   }) ;
});
