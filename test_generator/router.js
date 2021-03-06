'use strict';
const express = require('express');
const router = express.Router();

const index = require('./routers/index');
const users = require('./routers/users');
const img = require('./routers/img');
const request = require('./routers/request');
router.use('/', index);
router.use('/users', users);
router.use('/img', img);
router.use('/request', request);
module.exports = router;