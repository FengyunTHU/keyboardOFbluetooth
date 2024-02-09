// 定义图片原尺寸
let width_origin = 511;
let height_origin = 191;
// 定义一个原图片的对比尺寸
let lines;

function processCSVData(csvData) {
    lines = csvData.split('\n');
    for (var i = 0; i < lines.length; i++) {
        lines[i] = lines[i].replace(/\r+$/, '');
        lines[i] = lines[i].split(',');
    }
    console.log(lines);
}

document.addEventListener("DOMContentLoaded", function () {
    
    let img1 = document.querySelector("img.Img");
    let map1 = document.getElementById("image-map");

    if (lines && lines.length > 0) {
        SetCoords(img1, map1);
    }

    window.addEventListener("resize", function () {
        SetCoords(img1, map1);
    })
})

function SetCoords(img1, map1) {
    let newWidth = img1.clientWidth;
    let newHeight = img1.clientHeight;
    let lines_origin = lines;
    console.log(lines_origin);
    // console.log(newHeight, newWidth);

    // map1.style.width = newWidth + 'px';
    // map1.style.height = newHeight + 'px';

    // 定义伸缩比
    let chan = newWidth / width_origin;
    // 按照相似关系，满足：chan = new / origin → new = chan * origin
    var areas_all = map1.getElementsByTagName('area');
    console.log(areas_all);
    for (var i = 0; i < areas_all.length; i++) {
        let area_now = areas_all[i];
        let area_ori = lines_origin[i];
        let coords_now = area_now.getAttribute('coords').split(',');
        console.log("%d", i, coords_now);
        // var date = new Date();
        // console.log(date);
        for (var j = 0; j < coords_now.length; j = j + 1){
            coords_now[j] = chan * area_ori[j];
        }
        areas_all[i].setAttribute('coords', coords_now.join(','));
    }
}