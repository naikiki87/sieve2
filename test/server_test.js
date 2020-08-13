var io = require('socket.io').listen(52000);

console.log("Server running");

io.sockets.on('connection', function(socket) {
  socket.on('call', function(data) {
    console.log(data);
    socket.emit('answer', 'php hi');
  })
})
