var socket = require('socket.io');

socket.io.connect('http://localhost:52000');

socket.emit('call', 'hi node');

socket.on('answer', function(data) {
  console.log(data);
})
