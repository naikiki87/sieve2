'use strict';
const express = require('express');
const router = express.Router();
const wrapper = require('../modules/wrapper');
const db = require('../modules/db');
const config = require('../config');
const node_ssh = require('node-ssh');
const fs = require('fs-extra');
var fs2 = require('fs');
var multer = require('multer');
var xml2js = require('xml2js');
var mime = require('mime');
const session = require('express-session');
const app = express();
// var pyrun2 = require('./pyrun2.js')
// var pyrun = require('./pyrun.js')
var pyrun = require('../tasks/pyrun.js')


// import pyrun2 from './pyrun2'

app.use(session({
  key: 'sid',
  secret: 'secret',
  resave: false,
  saveUninitialized: true,
  cookie: {
    maxAge: 24000 * 60 * 60 // 쿠키 유효기간 24시간
  }
}));

var storage = multer.diskStorage({
    destination: (req, file, cb) => {
      cb(null, 'tasks/')
    },
    filename: (req, file, cb) => {
	  if (req.body.program_type ==0)
		  var t = ".py"
	  else if (req.body.program_type ==1)
		  var t = ".jar"
	  else if (req.body.program_type ==2)
		  var t = ".js"
	  else if (req.body.program_type ==3)
		  var t = ".xml"
	  else
		  var t = ".error"
		cb(null, req.body.name +  t)
    }
});
var upload = multer({storage: storage});
const up = upload.fields([{name: 'myFile', maxCount: 1}]);

function connectUser (user, callback) {
  var userSSH = new node_ssh();
  // 해당 연산서버의 유저에 접속
  userSSH.connect({
    host : user.ip_address,
    username : user.root_id,
    password : user.root_passwd,
    tryKeyboard: true,
    onKeyboardInteractive: (name, instructions, instructionsLang, prompts, finish) => {
      if (prompts.length > 0 && prompts[0].prompt.toLowerCase().includes('password')) {
        finish([user.root_passwd]);
      }
    }
  })
  .then((result) => {
    callback(user, userSSH);
  });
}
function copyPasteModules(user, userSSH, callback) {
  userSSH.putDirectory("./tasks", "/home/sieve_input_" + user.listening_port, {
    recursive : true,
    concurrency : 1,
    tick : (localPath, remotePath, error) => {
      if (error) {
        //console.log(error);
        userSSH.putFile(localPath, remotePath);
      };
    }
  })
  .then((status) => {
    userSSH.execCommand('chmod -R 777 /home/sieve_input_' + user.listening_port)
    .then((result) => {
       callback();
    });
  });
}
function copyPasteModules2(user, userSSH, callback) {
  userSSH.putDirectory("./sieveXML", "/home/uploadXML", {
    recursive : true,
    concurrency : 1,
    tick : (localPath, remotePath, error) => {
      if (error) {
        userSSH.putFile(localPath, remotePath);
      };
    }
  })
  .then((result) => {
    userSSH.execCommand('chmod -R 777 /home/uploadXML')
    .then((result) => {
      userSSH.execCommand('cd /home/uploadXML && cp DataDefinition.xml /home/sieve1/dist/config/DataDefinition.xml')
      .then((result) => {
        userSSH.execCommand('cd /home/uploadXML && cp task.xml /home/sieve1/dist/config/task.xml')
        .then((result) => {
          callback();
        });
      });
    });
  });
}
function runModules(user, db_info)
{
  console.log("RUN");
  console.log()
  connectUser(user, (user, userSSH) =>
  {
    console.log("sieve_input_" + user.listening_port + " connected!")
    userSSH.execCommand('pkill -f /home/sieve_input_' + user.listening_port)
    .then((result) =>
    {
      console.log("sieve_input_" + user.listening_port + " starting!")

      if (user.program_type == 0) // python
      {
        var execarr = [];
        execarr.push('python /home/sieve_input_' + user.listening_port + '/' + user.program_name+'.py')
      }
      if (user.program_type == 1) // java
      {
        var execarr = [];
        execarr.push('java -jar /home/sieve_input_' + user.listening_port + '/' + user.program_name+'.jar')
      }
      if (user.program_type == 2) // js
      {
        var execarr = [];
        execarr.push('node /home/sieve_input_' + user.listening_port + '/' + user.program_name+'.js')
      }
      if (user.program_type == 4) // sieve
      {
        userSSH.execCommand('yes | cp -rf /home/sieve_input_' + user.listening_port+'/'+user.program_name+'.xml ' + '/home/sieve_input_' + user.listening_port+'/dist/config/task.xml')
        .then((result) =>
        {
          userSSH.execCommand('yes | cp -rf /home/sieve_input_' + user.listening_port+'/'+user.program_name+'+_d.xml ' + '/home/sieve_input_' + user.listening_port+'/dist/config/DataDefinition.xml')
          .then((result) =>
          {
            userSSH.execCommand('echo ' + user.sieve_key + '/home/sieve_input_' + user.listening_port+'/dist/authstring')
            .then((result) =>
            {
              userSSH.execCommand('cd /home/sieve_input_' + user.listening_port + '/bin && ./revise.sh '+user.ip_address+' e'+user.listening_port+' ' + user.listening_port - 100)
              .then((result) =>
              {
                userSSH.execCommand('cd /home/sieve_input_' + user.listening_port + '/bin && ./exedep.sh & ./exedesc.sh &')
                .then((result) =>
                {
                });
              });
            });
          });
        });
        return
      }
      //if (user.program_type==4)
      //return

      execarr.push(user.job_id) // args[0]
      execarr.push(user.task_id)  // args[1]
      execarr.push(user.ip_address) // args[2]
      execarr.push(user.listening_port) // args[3]
      execarr.push(db_info[0].ip)
      execarr.push(db_info[0].name)
      execarr.push(db_info[0].id)
      execarr.push(db_info[0].passwd)
      execarr.push(user.output_type)

      var t_config = JSON.parse(user.config)

      if (user.output_type == 0) // socket
      {
        execarr.push(t_config.output_ip)
        execarr.push(t_config.output_port)
      }
      if (user.output_type == 1) // file
      {
        execarr.push(t_config.output_file_name)
      }
      if (user.output_type == 2) // db
      {
        execarr.push(t_config.db_ip)
        execarr.push(t_config.db_name)
        execarr.push(t_config.db_id)
        execarr.push(t_config.db_pwd)
        execarr.push(t_config.db_table_name)
      }

      console.log("COMMAND : ");
      console.log(execarr.join(" ") + " & ");
      userSSH.execCommand(execarr.join(" ") + " & ")
      .then((result) => {});
    });
  });
}

function runModules2(user, db_info)
{
  console.log("RUN2");
  connectUser(user, (user, userSSH) =>
  {
    console.log("sieve_input_" + user.listening_port + " connected!")
    userSSH.execCommand('pkill -f /home/sieve_input_' + user.listening_port)
    .then((result) =>
    {
      console.log("sieve_input_" + user.listening_port + " starting!")
      console.log("program type : ", user.program_type)

      if (user.program_type == 0) // python
      {
        var execarr = [];
        execarr.push('python /home/sieve_input_' + user.listening_port + '/' + user.program_name+'.py')
      }
      if (user.program_type == 1) // java
      {
        var execarr = [];
        execarr.push('java -jar /home/sieve_input_' + user.listening_port + '/' + user.program_name+'.jar')
      }
      if (user.program_type == 2) // js
      {
        var execarr = [];
        execarr.push('node /home/sieve_input_' + user.listening_port + '/' + user.program_name+'.js')
        var parameters = JSON.parse(user.config) 
        for (var i in parameters) {
          execarr.push(parameters[i])
        }
      }
      if (user.program_type == 4) // sieve
      {
        userSSH.execCommand('yes | cp -rf /home/sieve_input_' + user.listening_port+'/'+user.program_name+'.xml ' + '/home/sieve_input_' + user.listening_port+'/dist/config/task.xml')
        .then((result) =>
        {
          userSSH.execCommand('yes | cp -rf /home/sieve_input_' + user.listening_port+'/'+user.program_name+'+_d.xml ' + '/home/sieve_input_' + user.listening_port+'/dist/config/DataDefinition.xml')
          .then((result) =>
          {
            userSSH.execCommand('echo ' + user.sieve_key + '/home/sieve_input_' + user.listening_port+'/dist/authstring')
            .then((result) =>
            {
              userSSH.execCommand('cd /home/sieve_input_' + user.listening_port + '/bin && ./revise.sh '+user.ip_address+' e'+user.listening_port+' ' + user.listening_port - 100)
              .then((result) =>
              {
                userSSH.execCommand('cd /home/sieve_input_' + user.listening_port + '/bin && ./exedep.sh & ./exedesc.sh &')
                .then((result) =>
                {
                });
              });
            });
          });
        });
        return
      }
      
      var exe = execarr.join(' ')
      console.log("exe : ", exe)
      userSSH.execCommand(exe)
      .then((result) => {
        console.log(exe, "started")
      })
    });
  });
}

// 매개변수로 받은 유저의 모듈들 종료
function killModules(user, db_info)
{
  connectUser(user, (user, userSSH) =>
	{
		console.log("sieve_input_" + user.listening_port + " connected!")
		userSSH.execCommand('pkill -f /home/sieve_input_' + user.listening_port)
		.then((result) =>
		{
			console.log("sieve_input_" + user.listening_port + " killed!")
		});
	});
}

router.get('/pyrun', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("BE pyrun")  
  pyrun.pyrun();
  // pyrun();
  res.json({success: true});
}));

router.get('/logout', wrapper.asyncMiddleware(async (req, res, next) =>{
  res.redirect('http://localhost:8080/');
}));
router.post('/login', up, wrapper.asyncMiddleware(async (req, res, next) =>{
  var userID = req.body.userID;
  var userPW = req.body.userPW;
  console.log("");
  console.log("LOGIN");
  console.log(userID);
  console.log(userPW);
  console.log("");

  var result =  (await db.doQuery(`select * FROM users_test where userID = "${userID}"`));
  console.log("result", result[0].id);
  userPW = userPW.replace(/\n/g, "");//행바꿈제거
  if(userPW == result[0].password) {
    console.log("password correct");
    for(var i=0; i<10; i++) {
      // res.cookie(`user${i}`, userID , {
      res.cookie(`user${i}`, result[0].id , {
        expires: new Date(Date.now() + 3600000)
        // httpOnly: true
      });

    }
    // res.cookie("user", userID , {
    //   expires: new Date(Date.now() + 900000),
    //   httpOnly: true
    // });
    //
    // res.cookie("user2", userID , {
    //   expires: new Date(Date.now() + 900000),
    //   httpOnly: true
    // });
    // res.redirect('http://localhost:8080/main');
    res.redirect('/users');
  }
  else {
    res.redirect('http://localhost:8080/');
  }
}));

// var cookiecount = 0;
router.get('/', function(req, res, next) {
  console.log("RES USR");

  if(req.cookies) {
    console.log("Cookies");
    console.log(req.cookies);
  }

  res.redirect('http://localhost:8080/main');
})

router.get('/download/:fildid', function(req, res) {
  console.log("DoWnLoAd");
  var fildId = req.params.fileid;
  var origFileNm, savedFileNm, savedPath, fileSize;

  if(fildId == 1) {
    origFileNm = "test1.txt";
    savedFileNm = "test2.txt";
    savedPath = "D:/HTMLTEST";
    fileSize = "100";
  }

  var file = savedPath + '/' + savedFileNm;
  mimetype = mime.lookup(origFileNm);

  res.setHeader('Content-disposition', 'attachment; filename=' + origFildNm);
  res.setHeader('Content-type', mimetype);
  var filestream = fs.createReadStream(file);
  filestream.pipe(res);
})
router.post('/get_username', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;

  const user =  (await db.doQuery(`select * FROM users_test where id = ${id}`));
  res.json(user[0].userID);
}));
router.get('/engine_computer', wrapper.asyncMiddleware(async (req, res, next) => {

  console.log("engine Computer")
  const engine_computer = await db.doQuery('SELECT * FROM engine_computer');
  // res.json({success: true});
  res.json(engine_computer);
}));
router.post('/engine_computer/get_by_id', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body)
  const id = req.body.id;

  const engine_computer =  (await db.doQuery(`select * FROM engine_computer where id = ${id}`));
  res.json(engine_computer);
}));
router.post('/engine_computer/add', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body)
  const ip_address = req.body.ip_address;
  const root_id = req.body.root_id;
  const root_passwd = req.body.root_passwd;
  const sieve_key = req.body.sieve_key;
  const currentuserid = req.body.currentuserid;
  console.log(await db.doQuery(`INSERT INTO engine_computer (ip_address, root_id, root_passwd, sieve_key, userid) values ('${ip_address}','${root_id}','${root_passwd}','${sieve_key}', '${currentuserid}')`));
  res.json({success: true});
}));
router.post('/engine_computer2', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body)
  const searchTarget = req.body.searchTarget;
  const engine_computer =  (await db.doQuery(`select * FROM engine_computer where userid = ${searchTarget}`));
  // console.log(await db.doQuery(`INSERT INTO engine_computer (ip_address, root_id, root_passwd, sieve_key) values ('${ip_address}','${root_id}','${root_passwd}','${sieve_key}')`));
  res.json(engine_computer);
}));
router.post('/engine_computer/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM engine_computer where id = ${id}`));
  res.json({success: true});
}));
router.post('/engine_computer/delete_all', wrapper.asyncMiddleware(async (req, res, next) =>{

  console.log(await db.doQuery(`DELETE FROM engine_computer`));
  res.json({success: true});
}));

router.get('/cell_schemas', wrapper.asyncMiddleware(async (req, res, next) => {
  const cell_schemas = await db.doQuery('SELECT * FROM cell_schemas');
  res.json(cell_schemas);
}));
router.post('/cell_schemas2', wrapper.asyncMiddleware(async (req, res, next) =>{
  const currentuserid = req.body.currentuserid;

  const cell_schemas =  (await db.doQuery(`select * FROM cell_schemas where userid = ${currentuserid}`));
  res.json(cell_schemas);
}));
router.post('/cell_schemas/add', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body)
  const name = req.body.name;
  const comment = req.body.comment;
  const currentuserid = req.body.currentuserid;

  console.log(await db.doQuery(`INSERT INTO cell_schemas (name, comment, userid) values ('${name}','${comment}','${currentuserid}')`));
  res.json({success: true});
}));
router.post('/cell_schemas/xml2', wrapper.asyncMiddleware(async (req, res, next) => {
  var schemas = req.body.schemas;
  var attrs = req.body.attrs;
  console.log("SCHEMA ADD");
  console.log(schemas);
  console.log(attrs);
  var attr_num = 0;
  var path = '../Server/sieveXML/DataDefinition.xml';

  console.log("XML Write");
  console.log(schemas);
  console.log(attrs);

////////////////////// File create ///////////////////////////
  var text = `<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE ddl-list SYSTEM "DataDefinition.dtd">
<ddl-list>\n`;
  //fs.writeFileSync(__dirname + '/DataDefinition.xml', text);
  fs.writeFileSync(path, text);

////////////////////// write contents ///////////////////////////
  for(var i=0; i<schemas.length; i++) {
    var id = schemas[i].id;
    var name = schemas[i].name;
    console.log("id : " + id);
    // console.log("name : " + name);
    var text = `<ddl id="${id}">
<title> ${name} </title>
<owner>dblab</owner>
<creation-date>1579168326196</creation-date>
<comments/>
<attribute-format type="variable">
<field-delimeter>\\t</field-delimeter>
<record-delimeter>\\n</record-delimeter>
<itemset is-itemset-data="false" itemset-delimeter="">
<tid-index>0</tid-index>
</itemset>
</attribute-format>
<attribute-list>\n`;

    fs.appendFileSync(path, text);

    // console.log("ATTR LEN : " + attrs.length);

    for(var j=0; j<attrs.length; j++) {


      if(attrs[j].schema_id == schemas[i].id) {
        var id = attrs[j].id;
        var name = attrs[j].name;
        var type = attrs[j].type_id;
        console.log("type : " + type);
        if(type == 1) { type = "string"; }
        else if(type == 2) { type = "integer"; }
        else if(type == 3) { type = "double"; }
        else if(type == 4) { type = "long"; }
        //var text = `<attribute id="${id}">
        var text = `<attribute id="${attr_num}">
<name>${name}</name>
<type>${type}</type>
<allow-null>false</allow-null>
<default-value/>
</attribute>\n`;

        fs.appendFileSync(path, text);
        attr_num++;
      }
    }
    var text = `</attribute-list>
</ddl>\n`;
    fs.appendFileSync(path, text);

    attr_num = 0;

  }

////////////////////// File finish ///////////////////////////
  var text = `</ddl-list>`;
  fs.appendFileSync(path, text);

  res.redirect('/users/cell_schemas/to_sieve1');
}));
router.get('/cell_schemas/to_sieve1', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("TO SIEVE 1");
  var user = {
    ip_address : '165.132.105.40',
    root_id : 'root',
    root_passwd : 'elql2716'
  }
	connectUser(user, (user, userSSH) =>
	{
    console.log("sieve_input_file connected!");

    copyPasteModules2(user, userSSH, () =>
		{
			console.log("FILE INPUT DONE!");

      userSSH.execCommand('killall java')
      .then((result) => {
        userSSH.execCommand('cd /home/sieve1/bin && ./exedep.sh && ./exedesc.sh')
        .then((result) => {
          console.log("complete");
        })
      });

		});
  });

	// res.json({success: true});
  res.json({success: 1});
}));
router.post('/cell_schemas/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM cell_schemas where id = ${id}`));
  console.log(await db.doQuery(`DELETE FROM cell_columns where schema_id = ${id}`));
  res.json({success: true});
}));

router.get('/cell_columns/all', wrapper.asyncMiddleware(async (req, res, next) => {
  // const jobs = await db.doQuery('SELECT * FROM cell_columns');
  const jobs = await db.doQuery(`SELECT cc.*, dt.name AS type_name from cell_columns cc
join data_types dt on cc.type_id = dt.id`);
  res.json(jobs);
}));
router.post('/cell_columns', wrapper.asyncMiddleware(async (req, res, next) => {
  const schema_id = req.body.schema_id;
  // const cell_columns = await db.doQuery(`select * FROM cell_columns where schema_id = ${schema_id}`);
  const cell_columns = await db.doQuery(`SELECT cc.*, dt.name AS type_name from cell_columns cc
join data_types dt on cc.type_id = dt.id
where cc.schema_id = ${schema_id}`);

  res.json(cell_columns);
}));
router.post('/cell_columns/add', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body);
  const name = req.body.name;
  const schema_id = req.body.schema_id;
  const type_id = req.body.type_id;
  console.log(await db.doQuery(`INSERT INTO cell_columns (name, schema_id, type_id) values ('${name}','${schema_id}','${type_id}')`));
  res.json({success: true});
}));
router.post('/cell_columns/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM cell_columns where id = ${id}`));
  res.json({success: true});
}));

router.get('/jobs', wrapper.asyncMiddleware(async (req, res, next) => {
  const jobs = await db.doQuery('SELECT * FROM jobs');
  res.json(jobs);
}));
router.post('/jobs2', wrapper.asyncMiddleware(async (req, res, next) =>{
  const currentuserid = req.body.currentuserid;

  const jobs = await db.doQuery(`SELECT * FROM jobs where userid= ${currentuserid}`);
  res.json(jobs);
}));
router.post('/jobs/add', wrapper.asyncMiddleware(async (req, res, next) =>{
  const name = req.body.name;
  const schema_id = 0;
  const comment = req.body.comment;
  const currentuserid = req.body.currentuserid;

  console.log("JOBS");
  console.log(await db.doQuery(`INSERT INTO jobs (name,schema_id, comment, userid) values ('${name}','${schema_id}','${comment}', '${currentuserid}')`));
  res.json({success: true});
}));
router.post('/jobs/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM jobs where id = ${id}`));
  console.log(await db.doQuery(`DELETE FROM job_tasks where job_id = ${id}`));
  console.log(await db.doQuery(`DELETE FROM task_lines where job_id = ${id}`));
  res.json({success: true});
}));
router.post('/jobs/distribute', wrapper.asyncMiddleware(async (req, res, next) =>{
  var result = 0;
  const id = req.body.id;
  console.log("id : ", id)
  const engine_computer = await db.doQuery(`select * from job_tasks jt
  join jobs j on j.id = jt.job_id
  join tasks t on t.id = jt.task_id
  join engine_computer ec on ec.id = jt.ec_id where jt.job_id = ${id}`);

  // const engine_computer = await db.doQuery(`select * from job_tasks jt
  // join jobs j on j.id = jt.job_id
  // join tasks t on t.id = jt.task_id
  // join engine_computer ec on ec.id = jt.ec_id where jt.job_id = ${id} and not t.program_type = 3`);
  console.log("Eng Leng : " + engine_computer.length);

	for (var i =0; i <engine_computer.length; i++){
		// var index = i;
		var user = engine_computer[i]
		connectUser(user, (user, userSSH) => {
      console.log(result);
			console.log("sieve_input_" + user.listening_port + " connected!")
			copyPasteModules(user, userSSH, () => {
        console.log("sieve_input_" + user.listening_port + " move modules done!")
        res.json({success: 1});
			});
    });
	};

}));
router.post('/jobs/run', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;
  const db_info =  await db.doQuery("select * from db_info")
  // const engine_computer = await db.doQuery(`select *, t.name as program_name from job_tasks jt
  //   join jobs j on j.id = jt.job_id
  //   join tasks t on t.id = jt.task_id
  //   join engine_computer ec on ec.id = jt.ec_id where jt.job_id = ${id} and not program_type = 3`);

  const engine_computer = await db.doQuery(`select *, t.name as program_name from job_tasks jt
    join jobs j on j.id = jt.job_id
    join tasks t on t.id = jt.task_id
    join engine_computer ec on ec.id = jt.ec_id where jt.job_id = ${id}`);

  console.log("-------------- RUN -----------------")
  for (var i =0; i <engine_computer.length; i++){
    var index = i;
    var user = engine_computer[i]
    // runModules(user,db_info)
    runModules2(user,db_info)
  };
  res.json({success: true});
}));
router.post('/jobs/kill', wrapper.asyncMiddleware(async (req, res, next) =>{
const id = req.body.id;


const engine_computer = await db.doQuery(`select *, t.name as program_name from job_tasks jt
join jobs j on j.id = jt.job_id
join tasks t on t.id = jt.task_id
join engine_computer ec on ec.id = jt.ec_id where jt.job_id = ${id}`);
	for (var i =0; i <engine_computer.length; i++)
	{
		var index = i;
		var user = engine_computer[i]
		killModules(user)

	};
	res.json({success: true});
}));
router.get('/tasks', wrapper.asyncMiddleware(async (req, res, next) => {
  const tasks = await db.doQuery(`SELECT t.*, pt.name AS type_name from tasks t
join program_types pt on t.program_type = pt.id`);
  res.json(tasks);
}));

router.post('/tasks2', wrapper.asyncMiddleware(async (req, res, next) =>{
  const currentuserid = req.body.currentuserid;
  const tasks = await db.doQuery(`SELECT t.*, pt.name AS type_name from tasks t
join program_types pt on t.program_type = pt.id where userid=0 or userid= ${currentuserid}`);
  res.json(tasks);
}));

router.post('/get_taskinfo', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("/get_taskinfo")
  const id = req.body.id;
  // const taskinfo = await db.doQuery(`SELECT * from job_tasks where id=${id}`);
  const taskinfo = await db.doQuery(`SELECT jt.*, t.name FROM job_tasks jt, tasks t WHERE jt.id = ${id} AND jt.task_id = t.id`);
  res.json(taskinfo);
}));

router.post('/update_dest_info', wrapper.asyncMiddleware(async (req, res, next) =>{
  const start_id = req.body.start_id;
  const end_id = req.body.end_id;
  // const task_info = await db.doQuery(`SELECT * from job_tasks where id = ${end_id}`);
  const dest_info = await db.doQuery(`SELECT * FROM engine_computer ec, job_tasks jt
WHERE jt.id = ${end_id} AND ec.id = jt.ec_id `);

  // res.json(task_info);
  const dest_ip = dest_info[0].ip_address;
  const dest_port = dest_info[0].listening_port;
  
  console.log("dest info : ", dest_ip, dest_port)
  // console.log("ip : ", dest_info.data.ip_address)
  await db.doQuery(`UPDATE job_tasks SET dest_ip = '${dest_ip}', dest_port = ${dest_port} WHERE id = ${start_id}`);
  // res.json(dest_info)
  res.json({success: true});
}));

router.post('/tasks/add',up, wrapper.asyncMiddleware(async (req, res, next) =>{
  const name = req.body.name;
  const program_type = req.body.program_type;
  const comment = req.body.comment;
  const currentuserid = req.body.currentuserid;
  var sieve2 = 1;
  console.log("TASK ADD");
  console.log(name);
  console.log(program_type);
  console.log(comment);
  console.log(currentuserid);

  console.log(await db.doQuery(`INSERT INTO tasks (name,program_type, comment, userid, sieve2) values ('${name}','${program_type}','${comment}','${currentuserid}','${sieve2}')`));
  res.redirect('http://localhost:8080/main');
}));

router.post('/tasks/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM tasks where id = ${id}`));
  res.json({success: true});
}));

router.post('/job_tasks', wrapper.asyncMiddleware(async (req, res, next) => {
  const job_id = req.body.job_id;
  // const job_tasks = await db.doQuery(`select jt.*, t.name FROM job_tasks jt join tasks t on t.id = jt.task_id where jt.job_id = ${job_id}`);
  const job_tasks = await db.doQuery(`select jt.*, t.name, t.program_type FROM job_tasks jt join tasks t on t.id = jt.task_id where jt.job_id = ${job_id}`);
  res.json(job_tasks);
}));
router.post('/job_tasks/update', wrapper.asyncMiddleware(async (req, res, next) =>{

  console.log(" ---------------- TASK UPDATe ------------------");
  console.log(req.body)

  const id = req.body.id;
  // const job_id = req.body.job_id;
  const task_id = req.body.task_id;
  const input_schema_id = req.body.input_schema_id;
  const listening_port = req.body.listening_port;
  const ec_id = req.body.ec_id;
  const output_type = req.body.output_type;
  const config = req.body.config;
  const heartbeat_task_id = req.body.heartbeat_task_id;
  const heartbeat_job_id = req.body.heartbeat_job_id;
  const output_schema_id = req.body.output_schema_id;
  const position_x = req.body.position_x;
  const position_y = req.body.position_y;
  const linkto = req.body.linkto;
  const linkfrom = req.body.linkfrom;
  const dest_ip = req.body.dest_ip;
  const dest_port = req.body.dest_port;

  // dest_ip = '${dest_ip}', dest_port = ${dest_port}
  console.log(await db.doQuery(`UPDATE job_tasks SET task_id = ${task_id}, input_schema_id = ${input_schema_id}, listening_port = ${listening_port}, ec_id = ${ec_id}, output_type = ${output_type}, config = ${config}, heartbeat_task_id = ${heartbeat_task_id}, heartbeat_job_id = ${heartbeat_job_id}, output_schema_id = ${output_schema_id}, position_x = ${position_x}, position_y = ${position_y}, linkto = ${linkto}, linkfrom = ${linkfrom}, dest_ip = '${dest_ip}', dest_port = ${dest_port} WHERE id=${id}`));
  res.json({success: true});
}));
router.post('/job_tasks/add', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body)
  const job_id = req.body.job_id;
  const task_id = req.body.task_id;

  // const input_schema_id = req.body.input_schema_id;
  const input_schema_id = 1

  const listening_port = req.body.listening_port;
  const ec_id = req.body.ec_id;

  // const output_type = req.body.output_type;
  // const config = req.body.config;
  // const heartbeat_task_id = req.body.heartbeat_task_id;
  // const heartbeat_job_id = req.body.heartbeat_job_id;
  // const output_schema_id = req.body.output_schema_id;

  const output_type = 1
  const config = 1
  const heartbeat_task_id = 1
  const heartbeat_job_id = 1
  const output_schema_id = 1

  const position_x = req.body.position_x;
  const position_y = req.body.position_y;
  const dest_ip = req.body.dest_ip;
  const dest_port = req.body.dest_port;
  console.log(await db.doQuery(`INSERT INTO job_tasks (job_id, task_id, input_schema_id, listening_port, ec_id, output_type, config, heartbeat_task_id, heartbeat_job_id, output_schema_id, position_x, position_y, dest_ip, dest_port) values ('${job_id}','${task_id}','${input_schema_id}','${listening_port}','${ec_id}','${output_type}','${config}','${heartbeat_task_id}','${heartbeat_job_id}','${output_schema_id}','${position_x}','${position_y}', '${dest_ip}', '${dest_port}')`));
  res.json({success: true});
}));
router.post('/job_tasks/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("JOB DELETE START");
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM job_tasks where id = ${id}`));
  res.json({success: true});
}));
router.post('/job_tasks/delete_all', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("DELETE TASKS");
  const job_id = req.body.job_id;
  console.log(await db.doQuery(`DELETE FROM job_tasks where job_id = ${job_id}`));
  res.json({success: true});
}));

router.post('/job_tasks/xml2', wrapper.asyncMiddleware(async (req, res, next) => {
  const task_lines = JSON.parse(JSON.stringify(await db.doQuery(`select * FROM task_lines`)));
  var tasks = req.body.tasks;
  var s2tasks = req.body.s2tasks;
  const task_lines_select = [];

  console.log("1 tasks : ", tasks);
  console.log("2 tasks : ", s2tasks);

  for(var i=0; i<task_lines.length; i++) {
    for(var j=0; j<tasks.length; j++) {
      if(tasks[j][0] == task_lines[i].from_id) {
        task_lines_select.push(task_lines[i]);
      }
    }
  }

  console.log("xml2 lines : ", task_lines_select);
  console.log("lines length :", task_lines_select.length);

  var target = [];

  for(var i=0; i<task_lines_select.length; i++) {
    for(var j=0; j<s2tasks.length; j++) {
      if(task_lines_select[i].to_id == s2tasks[j][0]) {
        target.push(i);
      }
    }
  }
  for(var i=target.length-1; i>=0; i--) {
    console.log("target : " + target[i]);
    task_lines_select.splice(target[i], 1);
  }

  console.log("After");
  console.log("xml2 lines : ", task_lines_select);

  var path = '../Server/sieveXML/task.xml';
////////////////////// File create ///////////////////////////
  var text = `<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE task-list SYSTEM "task.dtd">
<task-list>
<algorithm-definition>
<algorithm-identifier id="1" mode="common" name="dblab.common.job.basestream.DefaultBasestream"/>
<algorithm-identifier id="2" mode="common" name="dblab.common.job.selectio.DefaultSelection"/>
<algorithm-identifier id="3" mode="mining" name="dblab.dsmm.job.estDec.EstDec"/>
<algorithm-identifier id="4" mode="mining" name="Sequence"/>
<algorithm-identifier id="5" mode="mining" name="Association"/>
<algorithm-identifier id="6" mode="common" name="dblab.common.job.Transactionalizer.Transactionalizer"/>
<algorithm-identifier id="7" mode="common" name="dblab.common.job.projection.Projection"/>
<algorithm-identifier id="8" mode="common" name="dblab.common.job.split.Split"/>
<algorithm-identifier id="9" mode="mining" name="dblab.dsmm.job.siblinglist.ClusteringOneDimension"/>
<algorithm-identifier id="10" mode="mining" name="dblab.dsmm.job.cstree.ClusteringFullDimension"/>
<algorithm-identifier id="101" mode="mining" name="dblab.dsmm.job.sttree.ClusteringSubDimension"/>
<algorithm-identifier id="11" mode="common" name="dblab.dsms.job.selection.BitSelection"/>
<algorithm-identifier id="12" mode="common" name="dblab.common.job.join.Bjoin"/>
<algorithm-identifier id="13" mode="common" name="dblab.common.job.join.MJoin"/>
<algorithm-identifier id="14" mode="common" name="dblab.common.job.alisa[pane]"/>
<algorithm-identifier id="15" mode="query processing" name="dblab.dsms.job.finaljob.Final"/>
<algorithm-identifier id="16" mode="query processing" name="dblab.dsms.job.analysis.Analysis"/>
<algorithm-identifier id="20" mode="olap" name="dblab.olap.job.myOlap.MyOlap"/>
<algorithm-identifier id="21" mode="common" name="dblab.common.job.alisa[multiplepane]"/>
<algorithm-identifier id="22" mode="mining" name="dblab.dsmm.job.topk.TopK"/>
<algorithm-identifier id="23" mode="mining" name="dblab.dsmm.job.estDec.EstDec64"/>
<algorithm-identifier id="24" mode="common" name="dblab.common.job.Union.Union"/>
<algorithm-identifier id="25" mode="mining" name="dblab.dsmm.job.ctxrule.ContextRule"/>
<algorithm-identifier id="26" mode="mining" name="dblab.dsmm.job.ctxseq.ContextSeq"/>
<algorithm-identifier id="27" mode="common" name="dblab.dsmm.job.velocity.Velocity"/>
<algorithm-identifier id="28" mode="common" name="dblab.common.job.mergeJob.MergeJob"/>
<algorithm-identifier id="29" mode="common" name="dblab.common.job.expression.ExpressionJob"/>
<algorithm-identifier id="30" mode="common" name="dblab.common.job.mjoin.TwoPhaseJoin"/>
<algorithm-identifier id="31" mode="common" name="dblab.common.job.join.HalfJoinPlus"/>
<algorithm-identifier id="32" mode="common" name="dblab.dsmm.job.estDec.EstMax"/>
<algorithm-identifier id="33" mode="common" name="dblab.common.job.period.Period"/>
<algorithm-identifier id="34" mode="common" name="dblab.dsmm.job.oneitemfrequent.OneItemFrequent"/>
<algorithm-identifier id="35" mode="common" name="dblab.dsmm.job.socketoutput.SocketOutput"/>
<algorithm-identifier id="36" mode="mining" name="dblab.dsmm.job.ctx.Context"/>
<algorithm-identifier id="1001" mode="common" name="dblab.common.job.join.KospiIndex"/>
</algorithm-definition>\n\n`;
  fs.writeFileSync(path, text);
////////////////////// write contents ///////////////////////////
// 1. Task(= Sieve2 Job)
  var text = `<task id="0" mode="DataStreamMining">
<name>test</name>
<owner>dblab</owner>
<creation-date>1579534534019</creation-date>
<description> </description>
<visible-other-user>true</visible-other-user>
<schedule-list> </schedule-list>
<preload-flag>true</preload-flag>\n\n`;
  fs.appendFileSync(path, text);

// 2. Job(= Sieve2 Task)
  var text = `<job-list>\n`;
  fs.appendFileSync(path, text);
  console.log("Leng : " + tasks.length);
  // console.log(tasks);

  for(var i=0; i<tasks.length; i++) {
    var id = tasks[i][0];
    var task_id = tasks[i][2];
    var in_schema = tasks[i][3];
    var listening_port = tasks[i][4];
    var config = tasks[i][7];
    var pos_x = tasks[i][11];
    var pos_y = tasks[i][12];
    var task_name = tasks[i][15];

    console.log("TASK : " + i);
    // BaseStream
    if(task_id == 0) {
      var text = `<job id="${id}" identifier="1" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>10</input-queue-size>
<parameter-list>
<data-definition-id>${in_schema}</data-definition-id>
<pipe-name>165.132.105.40:${listening_port}</pipe-name>
<number-of-blank>0</number-of-blank>
<system-timestamp-flag>false</system-timestamp-flag>
<ip-mapping-flag>false</ip-mapping-flag>
<mapping-table-flag>false</mapping-table-flag>
<delay>0</delay>
<abnormal-tuple-policy>discard</abnormal-tuple-policy>
</parameter-list>
<property-result-graph-setting-list/>
<export-list/>
</job>\n`;

fs.appendFileSync(path, text);
    }
    //Socket Output
    if(task_id == 1) {
      // console.log("TASK ID 1");
      var item = JSON.parse(config);

      var server = item.server;
      var port = item.port;
      var inputQueueSize = item.inputQueueSize;

      console.log("AAABBB");
      console.log(config);
      console.log(server);
      console.log(port);
      console.log(inputQueueSize);

      var text = `<job id="${id}" identifier="35" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>${inputQueueSize}</input-queue-size>
<parameter-list>
<number-of-blank>0</number-of-blank>
<data-definition-id>65</data-definition-id>
<pipe-name>${server}:${port}</pipe-name>
<system-timestamp-flag>false</system-timestamp-flag>
<ip-mapping-flag>false</ip-mapping-flag>
<mapping-table-flag>false</mapping-table-flag>
</parameter-list>
<property-result-graph-setting-list/>

<export-list/>
</job>\n`;
fs.appendFileSync(path, text);
    }
    // Projection
    if(task_id == 2) {
      var text = `<job id="${id}" identifier="7" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>10</input-queue-size>
<parameter-list>
<selected-attribute>${config}</selected-attribute>
</parameter-list>
<property-result-graph-setting-list/>
<export-list/>
</job>\n`;

      fs.appendFileSync(path, text);
    }
    // Aggregation
    if(task_id == 4) {
      var item = JSON.parse(config);
      // console.log(item);
      console.log("AGG");
      console.log(config);
      var inQSize = item[0].inQSize;
      var winType = item[0].winType;
      var winSize = item[0].winSize;
      var numPane = item[0].numPane;
      var sldSize = item[0].sldSize;
      var aggQuery = item[0].aggQuery;

      var numGB = item[1].length;
      var numHAV = item[2].length;
      var numSEL = item[3].length;
      var numOB = item[4].length;

      var text = `<job id="${id}" identifier="14" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>${inQSize}</input-queue-size>
<parameter-list>
<memory-management-type>on-demand</memory-management-type>
<memory-management-predefined>1000</memory-management-predefined>
<estimationtype>0</estimationtype>
<is-ms>false</is-ms>
<default-flag>0</default-flag>
<window-type>${winType}</window-type>
<window-size>${winSize}</window-size>
<number-of-pane>${numPane}</number-of-pane>
<sliding-size>${sldSize}</sliding-size>
<with-rownum>false</with-rownum>
<query-string>${aggQuery}</query-string>\n`;
      fs.appendFileSync(path, text);

      //// Group By 절
      if(numGB == 0) {
        var text = `<group-by-list/>\n`
        fs.appendFileSync(path, text);
      }
      else {
        var text = `<group-by-list>\n`;
        fs.appendFileSync(path, text);

        for(var i=0; i<numGB; i++) {
          var gbID = item[1][i].GBIndex;
          var text = `<index>${gbID}</index>\n`;
          fs.appendFileSync(path, text);
        }
        var text = `</group-by-list>\n`;
        fs.appendFileSync(path, text);
      }

      //// Having 절
      if(numHAV == 0) {
        var text = `<having-list/>\n`;
        fs.appendFileSync(path, text);
      }
      else {
        var text = `<having-list>\n`;
        fs.appendFileSync(path, text);

        for(var i=0; i<numHAV; i++) {
          var div = item[2][i].div;
          if(div == 0) {
            var id = item[2][i].id;
            var typeCode = item[2][i].typeCode;
            var relOP = item[2][i].relOP;
            var distinctFlag = item[2][i].distinctFlag;
            var constant = item[2][i].constant;

            var text = `<having>
<index>${id}</index>
<aggregation-operator>${typeCode}</aggregation-operator>
<distinct-flag>${distinctFlag}</distinct-flag>
<relation-operator>${relOP}</relation-operator>
<constant-type>integer</constant-type>
<constant>${constant}</constant>
</having>\n`;
            fs.appendFileSync(path, text);
          }
          else {
            var typeCode = item[2][i].typeCode;
            var text = `<binary-operator>${typeCode}</binary-operator>`;
            fs.appendFileSync(path, text);
          }
        }

        var text = `</having-list>\n`;
        fs.appendFileSync(path, text);
      }
      //// Select 절
      if(numSEL == 0) {
        var text = `<select-list/>\n`
        fs.appendFileSync(path, text);
      }
      else {
        var text = `<select-list>\n`;
        fs.appendFileSync(path, text);


        for(var j=0; j<numSEL; j++) {
          var text = `<select>\n`;
          fs.appendFileSync(path, text);
          var selID = item[3][j].id;
          var opCode = item[3][j].opCode;
          var distinctCode = item[3][j].distinctCode;
          var text = `<index>${selID}</index>
<aggregation-operator>${opCode}</aggregation-operator>
<distinct-flag>${distinctCode}</distinct-flag>\n`
          fs.appendFileSync(path, text);

          var text = `</select>\n`;
          fs.appendFileSync(path, text);
        }
        var text = `</select-list>\n`;
        fs.appendFileSync(path, text);
      }
      //// Order By 절
      if(numOB == 0) {
        var text = `<order-by-list>
<output-top>-1</output-top>
</order-by-list>
</parameter-list>
<property-result-graph-setting-list/>
<export-list/>
</job>\n`;
        fs.appendFileSync(path, text);
      }
      else {
        var text = `<order-by-list>\n`;
        fs.appendFileSync(path, text);

        for(var i=0; i<numOB; i++) {
          var index = item[4][i].itemID;
          var order = item[4][i].orderCode;
          var text = `<order-by>
<index>${index}</index>
<order>${order}</order>
</order-by>\n`;
          fs.appendFileSync(path, text);
        }

        var text = `<output-top>-1</output-top>
</order-by-list>
</parameter-list>
<property-result-graph-setting-list/>
<export-list/>
</job>\n`;
        fs.appendFileSync(path, text);
      }
    }
    // Transaction
    if(task_id == 6) {
      var item = JSON.parse(config);

      let inputQueueSize = item.inputQueueSize;
      let duration = item.duration;
      let addTimestamp = item.addTimestamp;
      let timestamp = item.timestamp;
      let tidAttr = item.tidAttr;
      let mergeAttr = item.mergeAttr;
      let mergeSep = item.mergeSep;

      var text = `<job id="${id}" identifier="6" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>${inputQueueSize}</input-queue-size>
<parameter-list>
<duration>${duration}</duration>
<add-timestamp-flag>${addTimestamp}</add-timestamp-flag>
<timestamp-flag>${timestamp}</timestamp-flag>
<tid-attribute-index>${tidAttr}</tid-attribute-index>
<merge-attribute-index>${mergeAttr}</merge-attribute-index>
<separator>${mergeSep}</separator>
</parameter-list>

<property-result-graph-setting-list/>

<export-list/>
</job>\n`;

      fs.appendFileSync(path, text);
    }

    // Binary Join
    if(task_id == 12) {
      var item = JSON.parse(config);

      // console.log("Binary Join");
      // console.log(item);

      var inputQueueSize = item.inputQueueSize;
      let attrIndex1 = item.attrIndex1;
      let attrIndex2 = item.attrIndex2;

      var from1 = item.schemaInfo[0].from_id;
      var from2 = item.schemaInfo[1].from_id;

      var item1 = item.schemaInfo[0].schema[attrIndex1].name;
      var item2 = item.schemaInfo[1].schema[attrIndex2].name;

      let winType1 = item.winType1? 'Rows' : 'Ranges';
      let winType2 = item.winType2? 'Rows' : 'Ranges';
      let winSize1 = item.winSize1;
      let winSize2 = item.winSize2;

      var text = `<job id="${id}" identifier="12" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>${inputQueueSize}</input-queue-size>
<parameter-list>
<join inner-buffer-size="1000" operation="equal">
<operand attribute="${item1}" stream="${from1}"/>
<operand attribute="${item2}" stream="${from2}"/>
</join>
<sources>

<source query="Q1" stream="${from1}" type="${winType1}" window="${winSize1}"/>
<source query="Q1" stream="${from2}" type="${winType2}" window="${winSize2}"/>
</sources>
<not_matched> </not_matched>
<condition>(  )</condition>
</parameter-list>
<property-result-graph-setting-list/>
<export-list/>
</job>\n`;
      fs.appendFileSync(path, text);
    }
    // Half join
    if(task_id == 31) {
      var item = JSON.parse(config);

      // console.log("HALF JOIN");
      // console.log(item);

      var parentAttrs = item.parentAttrs;
      var parentIDs = item.parentIDs;
      var attrIndex1 = item.AttrIndex1;
      var attrIndex2 = item.AttrIndex2;
      var attrIndex1_name = parentAttrs[0][attrIndex1].name;
      var attrIndex2_name = parentAttrs[1][attrIndex2].name;

      var type1 = item.type1? 'Table' : 'Stream';
      var type2 = item.type2? 'Table' : 'Stream';
      var inputQueueSize = item.InputQueueSize;
      var isEqual;
      var tempisEqual = item.isEqual;
      if(tempisEqual == 0) { isEqual == "equal"; }
      else { isEqual == "notequal"; }
      var booleanRemove = item.BooleanRemove? 'true' : 'false';
      var decayBase = item.DecayBase;
      var halfLife = item.HalfLife;
      var threshHold = item.ThreshHold;
      var matchAnd = item.MatchAnd? "Match And Replace" : "Match And Update";
      var attrIndex2Fixed = item.AttrIndex2Fixed;
      var attrIndex2Fixed_name = parentAttrs[1][attrIndex2Fixed].name;

      var text = `<job id="${id}" identifier="31" pos_x="${pos_x}" pos_y="${pos_y}">
<name>${id}</name>
<description> </description>
<input-queue-size>${inputQueueSize}</input-queue-size>
<parameter-list>
<decay-base>${decayBase}</decay-base>
<half-life>${halfLife}</half-life>
<threshold>${threshHold}</threshold>
<condition value=""/>
<boolean-remove>${booleanRemove}</boolean-remove>

<match>${matchAnd}</match>
<not_matched> </not_matched>

<join inner-buffer-size="1000" operation="${isEqual}">
<operand attribute="${attrIndex1_name}" stream="${parentIDs[0]}"/>
<operand attribute="${attrIndex2_name}" stream="${parentIDs[1]}"/>
</join>
<sources>
<source query="Q1" stream="${parentIDs[0]}" type="${type1}" window="0"/>
<source query="Q1" stream="${parentIDs[1]}" type="${type2}" window="0"/>
</sources>
<fixed-attributes>
<fixed-attribute attribute="${attrIndex2Fixed_name}" stream="${parentIDs[1]}"/>
</fixed-attributes>
<refresh-times/>
</parameter-list>
<property-result-graph-setting-list/>
<export-list/>
</job>\n`;
      fs.appendFileSync(path, text);
    }
  }

  var text = `</job-list>\n\n\n`;
  fs.appendFileSync(path, text);

////////////////////// Task lines ///////////////////////////
  var text = `<job-mapping-list>\n`;
  fs.appendFileSync(path, text);

  var output;
  for(var i=0; i<task_lines_select.length; i++) {
    var from = task_lines_select[i].from_id;
    var to = task_lines_select[i].to_id;
    var text = `<mapping parent-id="${from}">
<child-job id="${to}"/>
</mapping>\n`;
    fs.appendFileSync(path, text);
  }
  var text = `</job-mapping-list>\n`;
  fs.appendFileSync(path, text);

////////////////////// File finish ///////////////////////////
  var text = `</task>
</task-list>`;
  fs.appendFileSync(path, text);

  res.redirect('http://localhost:3000/users/cell_schemas/to_sieve1');
}));

router.post('/task_lines', wrapper.asyncMiddleware(async (req, res, next) => {
  const job_id = req.body.job_id;
  const task_lines = await db.doQuery(`SELECT * FROM task_lines where job_id = ${job_id}`);
  res.json(task_lines);
}));
router.post('/task_lines/findparent', wrapper.asyncMiddleware(async (req, res, next) => {
  const to_id = req.body.to_id;
  const task_lines = await db.doQuery(`SELECT * FROM task_lines where to_id = ${to_id}`);
  res.json(task_lines);
}));
router.post('/task_lines/add', wrapper.asyncMiddleware(async (req, res, next) =>{
	console.log(req.body)
  const job_id = req.body.job_id;
  const from_id = req.body.from_id;
  const to_id = req.body.to_id;

  console.log(await db.doQuery(`INSERT INTO task_lines (job_id, from_id, to_id) values ('${job_id}', '${from_id}', '${to_id}')`));
  res.json({success: true});
}));
router.post('/task_lines/delete', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("DELETE LINES");
  const id = req.body.id;
  console.log(await db.doQuery(`DELETE FROM task_lines where id = ${id}`));
  res.json({success: true});
}));
router.post('/task_lines/delete_joblines', wrapper.asyncMiddleware(async (req, res, next) =>{
  console.log("DELETE LINES");
  const job_id = req.body.job_id;
  console.log(await db.doQuery(`DELETE FROM task_lines where job_id = ${job_id}`));
  res.json({success: true});
}));
router.get('/move_files', wrapper.asyncMiddleware(async (req, res, next) => {
	const console_val = ""
	const engine_computer = await db.doQuery('SELECT * FROM engine_computer');
	for (var i =0; i <engine_computer.length; i++)
	{
		var index = i;
		var user = engine_computer[i]
		connectUser(user, (user, userSSH) =>
		{
			console.log("engine_" + user.input_port + " connected!")

          // 배포된 유저에 맞게 이상징후 판별 시스템 설정
			copyPasteModules(user, userSSH, () =>
			{
				console.log("engine_" + user.input_port + " move modules done!")

			});
        });

	};
	res.json({success: true});
}));
router.get('/run', wrapper.asyncMiddleware(async (req, res, next) => {


	const console_val = ""
	const engine_computer = await db.doQuery('SELECT * FROM engine_computer');
	for (var i =0; i <engine_computer.length; i++)
	{
		var index = i;
		var user = engine_computer[i]
		runModules(user)

	};
	res.json({success: true});
}));
router.post('/run', wrapper.asyncMiddleware(async (req, res, next) => {
	const id = req.body.id;

	const console_val = ""
	const engine_computer = await db.doQuery('SELECT * FROM engine_computer where id = ' + id);
	for (var i =0; i <engine_computer.length; i++)
	{
		var index = i;
		var user = engine_computer[i]
		runModules(user)

	};
	res.json({success: true});
}));
router.post('/kill', wrapper.asyncMiddleware(async (req, res, next) => {
	const id = req.body.id;

	const console_val = ""
	const engine_computer = await db.doQuery('SELECT * FROM engine_computer where id = ' + id);
	for (var i =0; i <engine_computer.length; i++)
	{
		var index = i;
		var user = engine_computer[i]
		killModules(user)

	};
	res.json({success: true});
}));

module.exports = router;
