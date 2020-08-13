var request = require('request');
var cheerio = require('cheerio');

request.get({url: 'https://finance.daum.net/domestic'}, function(err, res, body) {
  console.log(body);
  var $ = cheerio.load(body);

  var text = $('.price .down').text().trim();

  // console.log(text);

  // var arr = $('.tistory_recomm').children('.recomm_blog').children('.tit_subject');
  //
  // for(var i=0; i<arr.prevObject.length; i++) {
  //   console.log(arr.prevObject[i].children[0].next.childrent[3].children[0].data);
  // }
})
