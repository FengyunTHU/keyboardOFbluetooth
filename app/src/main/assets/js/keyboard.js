// 定义图片原尺寸
let width_origin = 431;
let height_origin = 174;
// 定义一个原图片的对比尺寸
let lines = null;
let linesReadyCallback = null;// 回调函数
let img1 = document.querySelector("img.Img");
let map1 = document.getElementById("image-map");
let testc = document.querySelector("p.test");

// 定义一个设置按钮target
let target_setButton = 1;//1->正常;2->set
let how_byn = 1;
let bttn = document.querySelector("label.label_set");
bttn.addEventListener("click", function () {
    if (how_byn === 1) {
        target_setButton = 2;
        bttn.innerHTML = "&nbsp;启用输入&nbsp;";
        how_byn = 0;
    } else if (how_byn === 0) {
        target_setButton = 1;
        bttn.innerHTML = "&nbsp;调整按键&nbsp;";
        how_byn = 1;
    }
})


let svg = null;
let svg_darkc = null;

function preSVG(svg_, svg_dark_) {
    svg = svg_;
    svg_darkc = svg_dark_;
}

function tarSVG() {
    Android.setSVG();
}

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
            // 目前已经没有圆形区域->放弃维护2024/3/28
            if (AreaAll[i].shape === 'circle') {
                btn.style.left = ImageRect.left + parseInt(Coords[0]) - parseInt(Coords[2]) + 'px';
                btn.style.top = ImageRect.top + parseInt(Coords[1]) - parseInt(Coords[2]) + 'px';
                btn.style.width = parseInt(Coords[2]) * 2 + 'px';
                btn.style.height = parseInt(Coords[2]) * 2 + 'px';
                btn.style.transition = "background-color 0.15s ease";
                btn.className = AreaAll[i].className;
                btn.addEventListener('mousedown', function () {
                    this.style.backgroundColor = "rgba(211,211,211,0.8)";
                });
                btn.addEventListener('mouseup', function () {
                    this.style.backgroundColor = 'transparent';
                    // 在松开时发送
                    if (target_setButton === 1) {
                        Vibra.vibraOnce();
                        Addd(AreaAll[i]);
                    } else if (target_setButton === 2) {
                        // 开启modal

                    }
                });
                // btn.onclick = function () {

                // }
            } else if (AreaAll[i].shape === 'rect') {
                // 设置键盘button与按下效果
                btn.style.left = ImageRect.left + parseInt(Coords[0]) + 'px';
                btn.style.top = ImageRect.top + parseInt(Coords[1]) + 'px';
                btn.style.width = (parseInt(Coords[2]) - parseInt(Coords[0])) + 'px';
                btn.style.height = (parseInt(Coords[3]) - parseInt(Coords[1])) + 'px';
                btn.style.transition = "background-color 0.15s ease";
                btn.className = AreaAll[i].className;
                btn.id = AreaAll[i].className;
                btn.addEventListener('touchstart', function (event) {
                    // if (document.body.style.backgroundColor === '') {
                    //     console.log("enter in touchstart");
                    //     setSVG_Gery(i, 1);
                    // } else {
                    //     setSVG_White(i, 1);
                    // }
                    this.isPressed = true;
                    event.stopPropagation();
                    if (document.body.style.backgroundColor === '') {
                        this.style.backgroundColor = "rgba(211,211,211,0.4)";
                    } else {
                        this.style.backgroundColor = "rgba(255,255,255,0.4)";
                    }

                    if (target_setButton === 1) {
                        if (xiushi.includes(this.className)) {// 存在修饰键
                            keyset.push(this.className);
                            console.log(1);
                            oneordouble = 1;
                        }
                        else {// 不在
                            if (keyset.length === 0) {// 没有修饰键
                                Vibra.vibraOnce();
                                console.log(2);
                                ifsetxiushi = 1;// 发送了修饰键
                                Addd(this.className);
                                this.timeoutId = setTimeout(() => {
                                    this.intervalId = setInterval(() => {
                                        Vibra.vibraOnce();
                                        Addd(this.className);
                                    }, 500);
                                }, 300);
                                // keyset = [];
                            } else if (keyset.length >= 1) {
                                keyset.push(this.className);
                                // 发送修饰键
                                console.log(3);
                                Addd(keyset.join('+'));
                                keyset = [];
                            }
                        }
                    }
                });
                btn.addEventListener('touchend', function (event) {
                    // if (document.body.style.backgroundColor === '') {
                    //     console.log("enter in touchend");
                    //     setSVG_Gery(i, 0);
                    // } else {
                    //     setSVG_White(i, 0);
                    // }
                    // 清除计时
                    // 对于修饰键在结束时按下
                    this.style.backgroundColor = 'transparent';
                    if (!this.isPressed) {
                        return;
                    }
                    if (xiushi.includes(this.className)) {
                        if (keyset.length >= 2) {
                            if (oneordouble === 1) {
                                oneordouble = 0;
                                console.log(4);
                                Addd(keyset.join('+'));
                                keyset = [];
                            }
                            // 发送组合键
                        } else if (keyset.length === 1) {
                            // 直接发送
                            Vibra.vibraOnce();
                            console.log(5);
                            Addd(this.className);
                            keyset = [];
                        }
                    } else {
                        keyset = [];
                    }
                    clearTimeout(this.timeoutId);
                    clearInterval(this.intervalId);
                    
                    // 在松开时发送
                    if (target_setButton === 2) {
                        // var top = event.clientY;
                        // 开启modal
                        showModal(this);
                    }
                });
                this.isPressed = false;
                // btn.onclick = function () {
                //     Vibra.vibraOnce();
                //     Addd(AreaAll[i]);
                // }
            }
        })(i);
        BTN_SET.appendChild(btn);
    }
}

// 存储组合键的集合
let keyset = [];
let ifsetxiushi = 0;// 是否发送修饰键
let oneordouble = 0;

function reset() {
    setButton();
    // 重设图片
    let img = document.querySelector('img.Img');
    let pict = document.querySelector('img.sunANDmoon');
    if (document.body.style.backgroundColor === '') {
        img.src = './img/svg.svg';
    } else {
        img.src = './img/svg_dark.svg';
    }
    pict.onclick = changepic;
    svg_bri = null;
    svg_dark = null;
}

function setSVG_Gery(i, activity) {
    if (svg === null) {
        // fetch('../img/svg.svg')
        //     .then(Response => Response.text())
        //     .then(data => {
        console.log("fetch_done_gery");
        // let parser = new DOMParser();
        // let svgDoc = parser.parseFromString(data, 'image/svg+xml');
        // svg = svgDoc.querySelector("svg");
        tarSVG();
        console.log(svg);
        let rectElement = svg.querySelector('#svg_' + (2 * i + 1).toString());
        if (activity === 1) {// 按下
            rectElement.setAttribute("fill", "rgba(211,211,211,0.8)");
        } else if (activity === 0) {
            rectElement.setAttribute("fill", "transparent");
        }
        // 将修改后的SVG内容转换为数据URL
        let svgData = new XMLSerializer().serializeToString(svg);
        let svgDataURL = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgData);
        // 获取<img>元素并设置其src属性
        let imgElement = document.getElementById('Img');
        imgElement.src = svgDataURL;
        // });
    } else if (svg != null) {
        console.log("fetch_done_gery");
        let rectElement = svg.querySelector('#svg_' + (2 * i + 1).toString());
        if (activity === 1) {
            rectElement.setAttribute("fill", "rgba(211,211,211,0.8)");
        } else if (activity === 0) {
            rectElement.setAttribute("fill", "transparent");
        }
        // 将修改后的SVG内容转换为数据URL
        let svgData = new XMLSerializer().serializeToString(svg);
        let svgDataURL = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgData);
        // 获取<img>元素并设置其src属性
        let imgElement = document.getElementById('Img');
        imgElement.src = svgDataURL;
    }
}

function setSVG_White(i, activity) {
    if (svg_darkc === null) {
        fetch('../img/svg_dark.svg')
            .then(Response => Response.text())
            .then(data => {
                let parser = new DOMParser();
                let svgDoc = parser.parseFromString(data, 'image/svg+xml');
                svg_darkc = svgDoc.querySelector("svg");
                let rectElement = svg_darkc.querySelector('#svg_' + (2 * i + 1).toString());
                if (activity === 1) {
                    rectElement.setAttribute("fill", "rgba(255,255,255,0.8)");
                } else if (activity === 0) {
                    rectElement.setAttribute("fill", "transparent");
                }
                // 将修改后的SVG内容转换为数据URL
                let svgData = new XMLSerializer().serializeToString(svg_darkc);
                let svgDataURL = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgData);
                // 获取<img>元素并设置其src属性
                let imgElement = document.getElementById('Img');
                imgElement.src = svgDataURL;
            });
    } else if (svg_dark != null) {
        let rectElement = svg_dark.querySelector('#svg_' + (2 * i + 1).toString());
        if (activity === 1) {
            rectElement.setAttribute("fill", "rgba(255,255,255,0.8)");
        } else if (activity === 0) {
            rectElement.setAttribute("fill", "transparent");
        }
        // 将修改后的SVG内容转换为数据URL
        let svgData = new XMLSerializer().serializeToString(svg_dark);
        let svgDataURL = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgData);
        // 获取<img>元素并设置其src属性
        let imgElement = document.getElementById('Img');
        imgElement.src = svgDataURL;
    }
}

function Addd(key) {
    // var key = area.className;
    bluetooth.sendKey(key);
}

// 存储shift信息
let mapofshift = {
    '1': '!',
    '2': '@',
    '3': '#',
    '4': '$',
    '5': '%',
    '6': '^',
    '7': '&',
    '8': '*',
    '9': '(',
    '0': ')',
    '-': '_',
    '=': '+',
    '[': '{',
    ']': '}',
    '\\': '|',
    ';': ':',
    '\'': '"',
    ',': '<',
    '.': '>',
    '/': '?'
}

// 存储修饰键集合
let xiushi = [
    'RIGHT_SHIFT',
    'LEFT_SHIFT',
    'LEFT_CTRL',
    'RIGHT_CTRL',
    'LEFT_ALT',
    'RIGHT_ALT',
    'HOME'
]

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

var hows = 1;
let bttns = document.querySelector("label.label_s");
bttns.addEventListener("click", function () {
    if (hows === 1) {
        showButton(1);
        bttns.innerHTML = "&nbsp;关闭显示&nbsp;";
        hows = 0;
    } else if (hows === 0) {
        showButton(2);
        bttns.innerHTML = "&nbsp;显示键位&nbsp;";
        hows = 1;
    }
})