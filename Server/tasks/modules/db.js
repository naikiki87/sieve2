const mysql = require('mysql');
const config = require('../config');
const connection = mysql.createPool(config.db);
exports.getQueryResult = (sql, mainConnections) => new Promise((resolve, reject) => {
  //console.log(sql);
  mainConnections.getConnection((err1, connection) => {
    if (err1) return reject(err1);
    return connection.query(sql, (err2, rows) => {
      connection.release();
      if (err2) return reject(err2);
      return resolve(rows);
    });
  });
});

exports.doQuery = (sql) => {
  return new Promise( (resolve, reject) => {
    connection.getConnection((err, connection) => {
      if(err) return reject(err);
      connection.query(sql, (err, rows) => {
        connection.release();
        if (err) return reject(err);
        resolve(rows);
      });
    });
  });
};

exports.bulk = (sql, values) => new Promise((resolve, reject) => {
  mainConnections.getConnection((err1, connection) => {
    if (err1) return reject(err1);
    return connection.query(sql, [values], (err2, rows) => {
      connection.release();
      if (err2) return reject(err2);
      return resolve(rows);
    });
  });
});

// const ojConnections = mysql.createPool(config.ojdb);
//
// exports.ojQuery = sql => new Promise((resolve, reject) => {
//   ojConnections.getConnection((err1, connection) => {
//     if (err1) return reject(err1);
//     return connection.query(sql, (err2, rows) => {
//       connection.release();
//       if (err2) return reject(err2);
//       return resolve(rows);
//     });
//   });
// });
