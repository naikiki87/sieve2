'use strict';
const express = require('express');
const router = express.Router();

const index = require('./routers/index');
const users = require('./routers/users');

router.use('/', index);
router.use('/users', users);

module.exports = router;