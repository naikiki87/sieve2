var fs = require('fs');
var net = require('net')
const args = process.argv;

var ip_address = args[2]
var port = args[3]
var data_per_second = args[4]

//var connection = net.createConnection(port, ip_address);
var connection = net.createConnection(port, ip_address, function() {
  console.log("Connection Complete");
});
var data = fs.readFileSync('zz.txt', 'utf8');
var d = data.toString().split("\n")
console.log("Length : " + d.length);

try
{
  var s_index = 0;
  var Interval_num = setInterval(function()
  {
    if (s_index > d.length - 1)
    {
      s_index = 0;
    }
    console.log(s_index + " : " + d[s_index]);
    //connection.write(d[s_index]);
    s_index ++;
  }, 1000 / data_per_second);
}
catch(e)
{
    console.log('Error:', e.stack);
}

    /*
   try
   {
      var connection = net.createConnection(port, ip_address);
      console.log("connection complete!")
      var s_index = 0;
      var Interval_num = setInterval(function()
      {
         if (s_index > d.length - 1)
            s_index = 0;
         connection.write(d[s_index]+"\n");
         console.log(d[s_index])
         s_index ++;
      }, 1000 / data_per_second);

   }
      catch (e) {console.log(e)}

}
catch(e)
{
    console.log('Error:', e.stack);
}
*/
