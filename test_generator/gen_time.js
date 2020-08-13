var fs = require('fs');
var net = require('net')
const args = process.argv;

var ip_address = args[2]
var port = args[3]
var data_per_second = args[4]

try
{
  var data = fs.readFileSync('z15.txt', 'utf8');
  var d = data.toString().split("\n")

  try
  {
    var connection = net.createConnection(port, ip_address);
    console.log("connection complete!")
    var s_index = 0;
    var Interval_num = setInterval(function()
    {
      if (s_index > d.length - 1) { s_index = 0; }
      let str = d[s_index];
      let strs = str.split('\t');
      var time = `${Math.round(Date.now()/1000)}`;
      strs.unshift(time);
      // console.log(strs);
      // strs[0] = `${Math.round(Date.now()/1000)}`;

      // console.log(strs);
      str = strs.join('\t');
      console.log(str);
      connection.write(str+"\n");
      s_index ++;
    }, 1000 / data_per_second);

  }
  catch (e) {console.log(e)}
}
catch(e){ console.log('Error:', e.stack);}
