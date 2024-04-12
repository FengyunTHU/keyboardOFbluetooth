// touchbar
function settouchbar() {
    let oridiv = document.querySelector('div#main');
    oridiv.style.display = 'none';
    let divnew = document.querySelector('div#touchbar');
    divnew.style.display = 'flex';
    spanWidth0 = span.getBoundingClientRect().width;
    var imgStyle = window.getComputedStyle(document.querySelector('.touchbar img'));
    var height = parseInt(imgStyle.height);
    var marginRight = parseInt(imgStyle.marginRight);
    spanWidth02 = spanWidth0 - 2 * (height + marginRight);
    reset();
}
var spanWidth0;
var spanWidth02;

function closetouchbar() {
    let oridiv = document.querySelector('div#main');
    oridiv.style.display = 'flex';
    let divnew = document.querySelector('div#touchbar');
    divnew.style.display = 'none';
    reset();
}

function StartBluetooth_forbar() {
    let IMGF = document.querySelector("img.Init_intouchbar");
    IMGF.style.transform = 'rotate(360deg)';
    let divnew = document.querySelector('div.touchbar');
    let init = document.createElement('img');
    let connect = document.createElement('img');
    init.className = 'init_bleinbar';
    connect.className = 'connect_bleinbar';
    init.src = './img/init_bar.png';
    connect.src = './img/connect_bar.png';
    init.onclick = function () { bluetooth.CallBluetooth(); }
    connect.onclick = function () { bluetooth.ConnectotherBluetooth(); }
    IMGF.parentNode.insertBefore(init, IMGF.nextSibling);
    IMGF.parentNode.insertBefore(connect, IMGF.nextSibling.nextSibling);
}

function deleteBle_forbar() {
    let IMGF = document.querySelector('img.Init_intouchbar');
    IMGF.style.transform = 'rotate(0deg)';
    let init = document.querySelector('img.init_bleinbar');
    let connect = document.querySelector('img.connect_bleinbar');
    init.parentNode.removeChild(init);
    connect.parentNode.removeChild(connect);
}

var if_bueisopen = 0;

cous = 0;
function SETBluetooth_forbar() {
    if (cous === 0) {
        StartBluetooth_forbar();
        if_bueisopen = 1;
        cous = 1;
    } else if (cous === 1) {
        deleteBle_forbar();
        if_bueisopen = 0;
        cous = 0;
    }
}


var hows2 = 1;
let bttns2 = document.querySelector("label.touchbar_set");
bttns2.addEventListener("click", function () {
    if (hows2 === 1) {
        settouchbar();
        bttns2.innerHTML = "&nbsp;Close&nbsp;";
        hows2 = 0;
    } else if (hows2 === 0) {
        closetouchbar();
        bttns2.innerHTML = "&nbsp;TouchBar&nbsp;";
        hows2 = 1;
    }
})

// var hows3 = 1;
// let bttns3 = document.querySelector("label.ani");
// bttns3.addEventListener("click", function () {
//     if (hows3 === 1) {
//         bttns3.innerHTML = "&nbsp;关闭特效&nbsp;";
//         hows3 = 0;
//     } else if (hows3 === 0) {
//         bttns3.innerHTML = "&nbsp;开启特效&nbsp;";
//         hows3 = 1;
//     }
// })

// // 特效注入or去除函数


let span = document.querySelector('span.nicewords');
let input = document.querySelector("input.myInput");
input.value = span.textContent;
span.onclick = function () {
    if (span.style.display !== 'none') {
        span.style.display = 'none';
        input.style.flexGrow = '1';
        input.style.display = 'inline-flex';
    }
}

var lastcontent = null;
var lastif = null;
document.addEventListener('click', function (event) {
    let divnew = document.querySelector('div.touchbar');
    if (event.target !== input && event.target !== span && divnew.style.display === 'flex') {
        if (divnew.style.display === 'flex') {
            console.log(divnew.style.display);
            input.style.display = 'none';
            span.style.display = 'inline-flex';
            span.textContent = input.value;
            if (span.textContent !== lastcontent || lastif !== if_bueisopen) {
                var height = span.clientHeight;
                if (window.innerHeight <= 450) {
                    span.style.fontSize = 35 + 'px';
                } else {
                    span.style.fontSize = 22 + 'px';
                }

                console.log(span.textContent);
                // 获取span元素nicewords

                // 创建一个临时元素来计算文字总长度
                var temp = document.createElement('span');
                temp.style.visibility = 'hidden';
                temp.style.whiteSpace = 'nowrap';
                if (window.innerHeight <= 450) {
                    temp.style.fontSize = '35px';
                } else {
                    temp.style.fontSize = '22px';
                }
                temp.textContent = span.textContent;
                let size = window.getComputedStyle(span, null).getPropertyValue('font-size');
                console.log("size" + size);
                document.body.appendChild(temp);
                var textWidth = temp.getBoundingClientRect().width;
                console.log("text" + textWidth);
                // 获取span的实际宽度
                var spanWidth = span.getBoundingClientRect().width;
                if (if_bueisopen === 1) {
                    spanWidth = spanWidth02;
                } else if (if_bueisopen === 0) {
                    spanWidth = spanWidth0;
                }
                console.log("span" + spanWidth);
                // 如果文字总长度大于span的实际宽度，缩小字体大小
                if (textWidth > spanWidth) {
                    var fontSize = parseInt(window.getComputedStyle(span, null).getPropertyValue('font-size'));
                    while (textWidth > spanWidth) {
                        fontSize--;
                        span.style.fontSize = fontSize + 'px';
                        temp.style.fontSize = fontSize + 'px';
                        textWidth = temp.getBoundingClientRect().width;
                    }
                }
                else {
                    console.log("eeeee");
                }
                document.body.removeChild(temp);
            }
        }
        lastcontent = span.textContent;
        lastif = if_bueisopen;
    }
    // var nicewords = document.querySelector('span.nicewords');
    // // 检测元素的总高度
    // var totalHeight = nicewords.scrollHeight;
    // // 如果总高度大于40px，缩小字体大小
    // var maxHeight = nicewords.style.height ? parseInt(nicewords.style.height) : 40;
    // if (totalHeight > maxHeight) {
    //     var fontSize = parseInt(window.getComputedStyle(nicewords, null).getPropertyValue('font-size'));
    //     while (totalHeight > maxHeight) {
    //         fontSize--;
    //         nicewords.style.fontSize = fontSize + 'px';
    //         totalHeight = nicewords.scrollHeight;
    //     }
    // }
})