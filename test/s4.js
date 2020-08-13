const args = process.argv;
const axios = require('axios');
var item = args[2];
var avPrice = args[3];
var ownstk = args[4];
var url = `https://finance.daum.net/api/quotes/A${item}?summary=false&changeStatistics=true`;
var cnt = 0;
var prev_price = 0;
var prev_volume = 0;

const getStockInfo = () => {
  try {
    return axios.get(url, {
      headers: {
        authority: "finance.daum.net",
        method: "GET",
        path: "/api/quotes/A005930?summary=false&changeStatistics=true",
        scheme: "https",
        accept: "application/json, text/javascript, */*; q=0.01",
        'accept-encoding': "gzip, deflate, br",
        'accept-language': "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7",
        cookie: "webid=d46730276e634ffaa5192c52a065e43a; SLEVEL=1; _TI_NID=iok9mjEFfnWoNqMjDA5hhZXcdWiWGmEAqFI+ku1SWnKso2aKSyJqQlbi2x0X7wUhZESA4O+tJ9qeqeHe3gi7tg==; ssab=823_1; _ga=GA1.2.742888705.1585551741; _gid=GA1.2.745483432.1585551741; KAKAO_STOCK_CHART_ENABLED_INDICATORS=[%22sma%22%2C%22column%22]; KAKAO_STOCK_RECENT=[%22A005930%22%2C%22A059090%22]; TIARA=eEjETbrkEXEWQNaOpVY6HIGHvnpVmY.jAdH-ZPTPYT6lfJqeJ6Z2dWN8DP4VlL6Pi7jIQl_ENkCRB_uOM9.6n7vO1J2f7rFe; _gat_gtag_UA_128578811_1=1; webid_sync=1585638058122; _dfs=Tll3Y2ZxRkdCWWZIMXp5K2w1NlJDSTZ1RWdzZVRlbWR4aXJMeUVrMU9mRDhvckhYU3pqVUFEeTRQZ055M2hXMjNYT3VDUS9KaXhrNmowUkJFelo0OFE9PS0tYUhDMFRxYUVRTUNaU0xRSHo2czNjZz09--db62e4303b7b5ead35e336c2a7112f994cc5284f",
        'if-none-match': `W/"41c52e677d2db77a499a831a5a51e77c"`,
        referer: "https://finance.daum.net/quotes/A005930",
        'sec-fetch-dest': "empty",
        'sec-fetch-mode': "cors",
        'sec-fetch-site': "same-origin",
        'user-agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36",
        'x-requested-with': "XMLHttpRequest"
      }
    });
  } catch (error) {
    console.error(error)
  }
};

// const main = () => {
function main() {
  // const rts = getStockInfo()
  getStockInfo()
  	.then(response => {
      var data = response.data;
      var dist_price = data.tradePrice - prev_price;
      var earn_price = data.tradePrice - avPrice;
      var earn_total = earn_price * ownstk;
      var dist_volume = data.accTradeVolume - prev_volume;
      console.warn(cnt, data.name, data.tradePrice, earn_price, earn_total);

      if(dist_volume >= 0 && data.tradePrice != prev_price) {
      // if(data.tradePrice != prev_price) {
        if(cnt == 10) {cnt = 0;}
        // console.log(cnt, data.name, data.tradePrice, data.accTradeVolume);
        console.warn(cnt, data.name, data.tradePrice, earn_price, earn_total);
        prev_price = data.tradePrice;
        prev_volume = data.accTradeVolume;
        cnt++;
      }
  	})
  	.catch(error => {
      console.log(error);
  	})
};

var Interval_num = setInterval(function(){
  main();
}, 100);
