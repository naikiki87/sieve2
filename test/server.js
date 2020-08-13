var net = require('net');
var textChunk = '';

var server = net.createServer(function(socket) {
  socket.write('Echo server\r\n');
  socket.on('data', function(data) {
    //console.log(data);
    textChunk = data.toString('utf8');
		console.log(textChunk);
    socket.write(textChunk);
  });

	//socket.pipe(socket);
});

server.listen(52275, '127.0.0.1');

console.log("Server listening : 1337");
