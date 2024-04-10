// 调用weather API来显示天气
let div_weather = document.querySelector('div.weather');

function getweather(location) {
    div_weather.textContent = "○...";
    // 如字符串"北京市"
    let url = "https://luckycola.com.cn/weather/geo?colaKey=KgZfCMBxFMau6q1712749230312e4QObCMIkb&address=" + location;
    fetch(url)
        .then(Response => Response.json())
        .then(data => {
            adcode = data.data.geocodes[0].adcode;
            console.log(adcode);
            url2 = "https://restapi.amap.com/v3/weather/weatherInfo?key=5596dc89d204a52ecf6aca3cb3454864&city=" + adcode + "&extensions=base";
            fetch(url2)
                .then(Response => Response.json())
                .then(data => {
                    console.log(data);
                    weather = data.lives[0].weather;
                    temperature = data.lives[0].temperature;
                    humidity = data.lives[0].humidity;
                    div_weather.textContent = weather + " " + temperature + " " + humidity;
            })
        })
}