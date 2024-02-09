let newdiv;

// 这是检测按键功能的js脚本
function Adddiv_test(key) {
    newdiv = document.createElement("div");
    newdiv.textContent = key;
    document.body.append(newdiv);
}

const mapElement = document.querySelector('map');
const areas = mapElement.getElementsByTagName('area');

for (let area of areas) {
    const areaClass = area.className;
    area.addEventListener('click', function() {
        Adddiv_test(areaClass);
    });
}