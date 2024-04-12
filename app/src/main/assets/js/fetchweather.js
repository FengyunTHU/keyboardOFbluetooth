// 调用weather API来显示天气
let div_weather = document.querySelector('div.weather');
let weather_pic = document.querySelector('img.weather_img');
let weather_font = document.querySelector('img.weather_font');
let svg_temp = '<svg width="450" height="115" xmlns="http://www.w3.org/2000/svg">' +
    '<g id="Layer_1">' +
    '<title>Layer 1</title>' +
    '<text fill="#FFFFFF" stroke-width="0" x="141" y="41.38461" id="city" font-size="37" font-family="\'Bitter\'" text-anchor="start" xml:space="preserve" stroke="#000">攀枝花市</text>' +
    '<text fill="#FFFFFF" stroke-width="0" x="349.65863" y="66.20025" id="weather" font-size="74" font-family="\'Bitter\'" text-anchor="start" xml:space="preserve" stroke="#000" transform="matrix(1.5183 0 0 1.42204 -201.375 1.10786)">晴</text>' +
    '<text fill="#FFFFFF" stroke="#000" stroke-width="0" x="7.69231" y="105.38461" id="temperature" font-size="65" font-family="\'Bitter\'" text-anchor="start" xml:space="preserve">21℃</text>' +
    '<text fill="#FFFFFF" stroke="#000" stroke-width="0" x="156.30769" y="98.61538" id="humidity" font-size="37" font-family="\'Bitter\'" text-anchor="start" xml:space="preserve">湿度: 1.5</text>' +
    '</g>' +
    '</svg>';

let parsers = new DOMParser();
let svginfp = parsers.parseFromString(svg_temp, 'image/svg+xml');
let svgElementinfo = svginfp.documentElement;

function getweather(location) {
    weather_pic.style.animation = "rotation 5.0s infinite linear";
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
                    weather_pic.style.animation = 'none';
                    weather_pic.style.transform = 'none';
                    weather_pic.src = './img/weathers/' + weather + '.svg';
                    let textcity = svgElementinfo.getElementById("city");
                    let textweather = svgElementinfo.getElementById('weather');
                    let texttemperature = svgElementinfo.getElementById('temperature');
                    let texthumidity = svgElementinfo.getElementById("humidity");
                    textcity.textContent = location;
                    textweather.textContent = weather;
                    let count = weather.length;
                    let size = textweather.style.fontSize;
                    textweather.style.fontSize = size / count;
                    texttemperature.textContent = temperature+'℃';
                    texthumidity.textContent = "湿度: " + humidity;
                    let serializer = new XMLSerializer();
                    let svgStr = serializer.serializeToString(svgElementinfo);
                    // 创建DataURL
                    let dataUrl = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgStr);
                    weather_font.src = dataUrl;
                })
        })
}