var PythonShell = require('python-shell');
var options = {
    mode: 'text',
    pythonPath: '',
    pythonOptions: ['-u'],
    scriptPath: '',
    args: ['value1', 'value2', 'value3']
  };

  module.exports = {
    pyrun : function() {
      PythonShell.PythonShell.run('routers/test1.py', options, function (err, results) {
        if (err) throw err;
        console.log('results: ', results);
      });
    }
  }