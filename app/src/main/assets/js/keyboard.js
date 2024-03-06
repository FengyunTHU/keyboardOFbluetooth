// 定义图片原尺寸
let width_origin = 511;
let height_origin = 191;
// 定义一个原图片的对比尺寸
let lines = null;
let linesReadyCallback = null;// 回调函数
let img1 = document.querySelector("img.Img");
let map1 = document.getElementById("image-map");
let testc = document.querySelector("p.test");

function processCSVData(csvData) {
    lines = csvData.split('\n');
    for (var i = 0; i < lines.length; i++) {
        lines[i] = lines[i].replace(/\r+$/, '');
        lines[i] = lines[i].split(',');
    }
    console.log(lines);
    //    testc.textContent = csvData;
    if (linesReadyCallback) {
        linesReadyCallback();
    }
}

document.addEventListener("DOMContentLoaded", function () {
    linesReadyCallback = function () {
        if (lines !== null && lines.length > 0) {
            SetCoords(img1, map1);
            setButton();
        }
    }
    window.addEventListener("resize", function () {
        SetCoords(img1, map1);
        setButton();
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
        for (var j = 0; j < coords_now.length; j = j + 1) {
            coords_now[j] = chan * area_ori[j];
        }
        areas_all[i].setAttribute('coords', coords_now.join(','));
    }
}

function setButton() {
    let Image = document.querySelector("img.Img");// img
    let Map = document.getElementById("image-map");// map
    var AreaAll = Map.getElementsByTagName('area');
    var ImageRect = Image.getBoundingClientRect();
    var BTN_SET = document.getElementById('BTN_SET');
    // 首先清除按钮
    BTN_SET.innerHTML = '';
    // 依次添加按钮
    for (var i = 0; i < AreaAll.length; i++) {
        var btn = document.createElement('button');
        btn.style.position = 'absolute';
        btn.style.borderRadius = AreaAll[i].shape === 'circle' ? '50%' : '0';
        btn.style.backgroundColor = 'transparent';
        btn.style.borderColor = 'transparent';
        var Coords = AreaAll[i].coords.split(',');
        (function (i) {
            if (AreaAll[i].shape === 'circle') {
                btn.style.left = ImageRect.left + parseInt(Coords[0]) - parseInt(Coords[2]) + 'px';
                btn.style.top = ImageRect.top + parseInt(Coords[1]) - parseInt(Coords[2]) + 'px';
                btn.style.width = parseInt(Coords[2]) * 2 + 'px';
                btn.style.height = parseInt(Coords[2]) * 2 + 'px';
                btn.onclick = function () {
                    Addd(AreaAll[i]);
                }
            } else if (AreaAll[i].shape === 'rect') {
                btn.style.left = ImageRect.left + parseInt(Coords[0]) + 'px';
                btn.style.top = ImageRect.top + parseInt(Coords[1]) + 'px';
                btn.style.width = (parseInt(Coords[2]) - parseInt(Coords[0])) + 'px';
                btn.style.height = (parseInt(Coords[3]) - parseInt(Coords[1])) + 'px';
                btn.onclick = function () {
                    Addd(AreaAll[i]);
                }
            }
        })(i);
        BTN_SET.appendChild(btn);
    }
}



function Addd(area) {
    var key = area.className;
    bluetooth.sendKey(key);
}

function showButton(target) {
    let BTN_SET = document.getElementById('BTN_SET');
    let BUTNS = BTN_SET.querySelectorAll('button');
    for (var j = 0; j < BUTNS.length; j++) {
        if (target === 1) {
            BUTNS[j].style.borderColor = 'red';
        } else if (target === 2) {
            BUTNS[j].style.borderColor = 'transparent';
        }
    }
}