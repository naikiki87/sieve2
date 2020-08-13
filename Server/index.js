'use strict';
const express = require('express');
const logger = require('morgan');
const bodyParser = require('body-parser');
const http = require('http');
const path = require('path');
const router = require('./router');
const config = require('./config');
const cors = require('cors'); // 크로스 브라우징
const cookieParser = require('cookie-parser');
// const session = require('express-session');

const app = express();

app.use(cors());
app.use(cookieParser());
// app.use(session({
//   key: 'sid',
//   secret: 'secret',
//   resave: false,
//   saveUninitialized: true,
//   cookie: {
//     maxAge: 24000 * 60 * 60 // 쿠키 유효기간 24시간
//   }
// }));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(express.static(path.join(__dirname, 'public')));
//app.use(express.static(path.join(__dirname, 'src')));
app.use(logger('dev'));

app.use('/', router);

app.use((req, res, next) => {
  let err = new Error('Not Found');
  err.status = 404;
  next(err);
});

app.use((err, req, res, next) => {
  console.log(err);

  res.status(err.status || 500);
  res.send(err);
});

const server = http.createServer(app);




server.listen(config.port);
console.log(`server start on port ${config.port}`);
