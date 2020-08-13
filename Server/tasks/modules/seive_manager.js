const config = require('../seive_config.json');
const node_ssh = require('node-ssh');
const fs = require('fs');
const path = require('path');
const SSH = require('simple-ssh');

const rootSSH = new node_ssh();
const format = require('string-format');
format.extend(String.prototype, {});

function addUser(user, root_info, port, callback) {
  rootSSH.connect(root_info).then(() => {
    rootSSH.execCommand('useradd ' + user)
    .then((result) => {
	 rootSSH.execCommand("chmod 777 home/" + user)
     rootSSH.execCommand("echo '{}' | passwd --stdin {}".format(config.user_passwd, user))
      .then((result) => {
        callback();
      });
    });
  });
}



function moveFile(userSSH, file_list, user)
{
	//console.log(file_list);
	var appRoot = require('app-root-path');
	
	if( typeof file_list[0] !='undefined' && file_list[0].length >0)
		userSSH.putFile(appRoot.path+"/xml/" + file_list[0], "/home/" + user + "/xml/"+file_list[0]).then(function(){
			console.log(file_list[0])
			file_list.shift()
			if (file_list.length >0)
				moveFile(userSSH, file_list, user)
			
		})
}

function connectUser(user, host, port, callback) {
  var userSSH = new node_ssh();
  userSSH.connect({
    host : host,
    username : user,
    password : config.user_passwd
  }).then(() => {
    callback(userSSH);
  }, function()
	  {
		  setTimeout(function(){
			  connectUser(user, host, port, callback)
		  }, 3000)
		  
	  });
}

function addSeiveUser (accountID, root_info, port, callback) {
  const user = 'cq_user'+accountID

  //console.log("adding " + user + "...")
  addUser(user, root_info, port, () => {
	// console.log("done adding " + user)
    connectUser(user, root_info.host, port, (userSSH) => {
      copyPasteSeive(userSSH, user, accountID, root_info.host, () => {
		//  console.log('done ' + accountID)
		  callback();
        //runSeive(userSSH, user);
      });
    });
  });
}
function addSeiveUserFromArray (account_list)
{
	var accountID = account_list[0].accountID
	var root_info = account_list[0].root_info
	var authstring = account_list[0].authstring
	var port = account_list[0].port
	const user = 'cq_user'+accountID

  //console.log("adding " + user + "...")
  addUser(user, root_info, port, () => {
	 console.log("done adding " + user)
    connectUser(user, root_info.host, port, (userSSH) => {
      copyPasteSeive(userSSH, user, accountID, root_info.host, authstring, () => {
		  account_list.shift();
		 // console.log(account_list)
		  if(account_list.length > 0)
			addSeiveUserFromArray(account_list);
		  else
			console.log("Done making user!")
        //runSeive(userSSH, user);
      });
    });
  });
	
	
}

function copyPasteSeive(userSSH, user, accountID, host,authstring, callback) {
  userSSH.putDirectory(config.seive.local_path, config.seive.remote_path.format(user), {
    recursive : true,
    concurrency : 1,
    validate : (itemPath) => {
      const baseName = path.basename(itemPath);
      return baseName.substr(0, 1) != '.' && baseName != 'node_modules';
    },
    tick : (localPath, remotePath, error) => {
      if (error) {
        userSSH.putFile(localPath, remotePath);
      //  console.log(localPath, remotePath);
      }
    }
  })
  .then((status) => {
    //console.log(status);

    userSSH.execCommand('chmod -R 777 /home/'+user+'/*')
    .then((result) => {
		//console.log(result)
			userSSH.execCommand(config.seive.authstring.format(authstring), {cwd : config.seive.dist_path.format(user)})
			  .then((result) => {
							userSSH.execCommand(config.seive.revise.format(host, user, (parseInt(accountID) * 100 + 30091).toString() ), {cwd : config.seive.bin_path.format(user)})
							.then((result) => 
							{
								userSSH.execCommand(config.seive.node_run.format((parseInt(accountID) * 100 + 30089).toString(), (parseInt(accountID) * 100 + 30090).toString()), {cwd : config.seive.node_path.format(user)})
								.then((result) => 
								{
									console.log(result);
									callback();
								})
							})
		 });
    })
  });
}
function runSeive(user_info) 
{
	const user = 'cq_user'+user_info.ma_id
	const host = user_info.ip_address;
	var have_to_run_file_name = user_info.ma_id + '_' + user_info.group_id + '_' + user_info.timezone + '.xml'; 
	connectUser(user, user_info.ip_address, 22, (userSSH) => 
	{
		userSSH.execCommand('chmod 777 *; for filename in '+have_to_run_file_name+'; do yes | cp -rf "$filename" "../dist/config/task.xml"; done;', {cwd : config.seive.xml_path.format(user)})
		.then((result) => 
		{
			console.log(result);
			userSSH.execCommand(config.seive.kill, {cwd : config.seive.bin_path.format(user)})
			.then((result) => 
			{

				userSSH.execCommand(config.seive.run, {cwd : config.seive.bin_path.format(user)})
				.then((result) => 
				{
				});
			});

		})
		
		
    });
  
}
exports.runSeive = function(user_info)
{
	runSeive(user_info);
}

function kill(user_info) 
{
	const user = 'cq_user'+user_info.ma_id
	connectUser(user, user_info.ip_address, 22, (userSSH) => 
	{
		userSSH.execCommand(config.seive.kill, {cwd : config.seive.bin_path.format(user)})
    });
  
}
exports.kill = function(user_info)
{
	kill(user_info);
}
exports.addSeiveUserFromArray = function(account_list)
{
	var accountID = account_list[0].accountID
	var root_info = account_list[0].root_info
	var authstring = account_list[0].authstring
	var port = account_list[0].port
	const user = 'cq_user'+accountID

  console.log("adding " + user + "...")
  //console.log(root_info)
  addUser(user, root_info, port, () => {
	 console.log("done adding " + user)
    connectUser(user, root_info.host, port, (userSSH) => {
      copyPasteSeive(userSSH, user, accountID, root_info.host,authstring, () => {
		  //console.log('done ' + accountID)
		  account_list.shift();
		 // console.log(account_list)
		  if(account_list.length > 0)
			addSeiveUserFromArray(account_list);
        //runSeive(userSSH, user);
      });
    });
  });
	
	
}
exports.distributeXMLFromArray = function(user_info, file_list)
{
	const user = 'cq_user'+user_info.ma_id
	//console.log(file_list)
 connectUser(user, user_info.ip_address, 22, (userSSH) => {
 userSSH.execCommand("find . -name '*.xml' -exec rm -rf {} \;", {cwd : config.seive.xml_path.format(user)})
	  .then((result) => {
		   moveFile(userSSH,file_list,user);
	  });
	
    });
}