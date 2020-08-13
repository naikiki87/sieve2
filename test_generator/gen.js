var fs = require('fs');
var net = require('net')
const args = process.argv;

var ip_address = args[2]
var port = args[3]
var data_per_second = args[4]
var data = fs.readFileSync('z4.txt', 'utf8');
var d = data.toString().split("\n")

// console.log(d);
// console.log(d.length);

try
{
  var connection = net.createConnection(port, ip_address);
  console.log("connection complete!")
  var s_index = 0;
  var Interval_num = setInterval(function()
  {
    if (s_index > d.length - 1)
    s_index = 0;
    let str = d[s_index];

    // let strs = str.split('\t');
    // strs.splice(2,2);

    console.log(s_index + " : " + str);

    connection.write(str + '\n');

    //// let strs = str.split('\t');

    // console.log(strs[0]);
    // console.log(strs[1]);
    // console.log(strs[2]);
    // console.log(strs[3]);

    //console.log(strs);

    //strs.splice(2,2);

    //console.log(strs);

    // str = strs.join('\t');
    // let str3 = strs.join('\t');

    // console.log(str3);
    //// connection.write(str3 + '\n');


    //connection.write(str+"\n");
    s_index ++;
  }, 1000 / data_per_second);
}
catch (e) {
  console.log(e);
}
