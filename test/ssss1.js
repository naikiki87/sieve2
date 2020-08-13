const axios = require("axios");
const cheerio = require("cheerio");

// axios를 활용해 AJAX로 HTML 문서를 가져오는 함수 구현
async function getHTML() {
  try {
    // return await axios.get("https://finance.naver.com/item/sise.nhn?code=059090");
    // return await axios.get("http://finance.daum.net/quotes/A059090#home");
    // return await axios.get("http://www.inu.ac.kr/com/cop/mainWork/foodList1.do?siteId=inu&id=inu_050110010000&command=week");
    return await axios.get("http://mlbpark.donga.com/mp/b.php?p=1&m=list&b=bullpen&query=&select=&user=");


  } catch (error) {
    console.error(error);
  }
}

var cnt=0;
var text;

var Interval_num = setInterval(function(){
  getHTML()
  .then(html => {
    const $ = cheerio.load(html.data);

    const bodylist = $('.bullpenbox');
    console.log(bodylist);

    $('table').find('tr').each(function(i, elem) {
      if(i<6) {}
      else {
        // var category = $(this).find('.t_left').find('title').text().trim();
        var category = $(this).find('.bullpenbox').find('title').text().trim();
        var title = $(this).find('.t_left').next().text().trim()
        // console.log($(this).find('.t_left').text().trim());
        // console.log(title);
        // console.log(category);
      }
    })

    // $('table').find('tr').each(function(i, elem) {
    // $('.tbList').find('tr').each(function(i, elem) {
    // $('.numB').find('i').each(function(i, elem) {
    //   console.log($(this).text().trim());
    // })
    // var body = $(".long").find('strong').text().trim();
    // console.log(body);
    // console.log(body);

    // body.each(function(i, elem) {
    //   text = $(this).find('strong').text().trim();
    // })
    // // var text = body.find('strong').text().trim();
    // console.log(text);

    // var text = $("#_nowVal").text().trim();
    // console.log(cnt, text);
    // cnt++;
  })
}, 3000);
