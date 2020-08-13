var request = require('request');
var cheerio = require('cheerio');

request.get({url: 'https://finance.naver.com/item/main.nhn?code=005930'}, function(err, res, body) {
  // console.log(body);
  var $ = cheerio.load(body);

  // var bodylist = $("#_nowVal").text().trim();

  // var text = $('.price .down').text().trim();
  // var text = $("#_nowVal").text().trim();
  var text = $(".sp_txt9").text().trim();
  // console.log(cnt, text);
  // cnt++;

  console.log(text);

  // console.log(text);

  // var arr = $('.tistory_recomm').children('.recomm_blog').children('.tit_subject');
  //
  // for(var i=0; i<arr.prevObject.length; i++) {
  //   console.log(arr.prevObject[i].children[0].next.childrent[3].children[0].data);
  // }
})
