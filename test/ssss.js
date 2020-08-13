const request = require('request');
const cheerio = require('cheerio');

const url = "http://www.inu.ac.kr/com/cop/mainWork/foodList1.do?siteId=inu&id=inu_050110010000&command=week";

request(url, (error, response, body) => {
  if (error) throw error;

  let titleList = [];
  let $ = cheerio.load(body);

  // console.log(body);
  const bodyList = $('table').children();

  console.log(bodyList);

  try {
    let krDay = '';
    let corner = '';
    let menu = '';

    // console.log(bodyList);

    bodyList.each(function(i, elem) {
      corner = $(this).find('th').text().trim();
      menu = $(this).find('th').next().text().trim();


      // console.log(`${corner} -> ${menu}`);
      // console.log(i, $(this).find('tr').text().trim());
      // titleList[i] = {
      //   title:$(this).find('tr').text();
      // }
    })

    // console.log(titleList);

    // $('table').find('tr').each(function (index, elem) {
    //   if (index % 6 === 0) {
    //     krDay = $(this).find('th').text().trim();
    //
    //     console.log(`${krDay}`);
    //   } else {
    //     corner = $(this).find('th').text().trim();
    //     menu = $(this).find('th').next().text().trim();
    //
    //     console.log(`${corner} -> ${menu}`);
    //   }
    // });
  } catch (error) {
    console.error(error);
  }
});

// const request = require('request');
//
// const url = "http://www.inu.ac.kr/com/cop/mainWork/foodList1.do?siteId=inu&id=inu_050110010000&command=week";
//
// request(url, (error, response, body) => {
//   if (error) throw error;
//
//   console.log(body);
// });
