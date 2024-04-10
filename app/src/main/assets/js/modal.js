let modal = document.getElementById("myModal");
let modalContent = document.getElementById("modal-content-pick");
let svg_bri = null;
let svg_dark = null;

// 隐藏窗口
window.onclick = function (event) {
    if (event.target === modal && event.target !== modalContent) {
        modal.style.transition = "opacity 0.2s, visibility 0s 0.2s";
        modal.style.opacity = "0";
        modal.style.visibility = 'hidden';
    }
}

// 显示窗口
function showModal(button) {
    // modalContent.style.top = top + 'px';
    // console.log(top);
    modal.style.transition = "opacity 0.2s, visibility 0.2s 0s";
    modal.style.opacity = "1";
    modal.style.visibility = "visible";
    chooseKEY(button);
}

// 选择键位
function chooseKEY(button) {// 点击按钮的button
    // 用图片来选择
    let svg_cho = document.querySelector("img.svg_cho");
    let rect = svg_cho.getBoundingClientRect();
    let y = rect.top + window.scrollY;
    let x = rect.left + window.scrollX;
    svg_cho.useMap = "#image-maps";
    let map = document.createElement('map');
    map.name = 'image-maps';
    svg_cho.after(map);

    var height_ori = 29;
    var new_height = svg_cho.clientHeight;
    var chan2 = new_height / height_ori;
    var width_new = 25 * chan2;
    var inter_new = 28 * chan2;
    var start_new = 2 * chan2;
    // 共45个选择
    for (i = 0; i < 26; i++) {
        (function (i) {
            // 依次添加按钮
            let area = document.createElement('area');
            area.shape = 'rect';
            area.coords = `${start_new + inter_new * i},${start_new},${start_new + inter_new * i + width_new},${start_new + width_new}`;
            area.className = keymap[i + 1];
            area.addEventListener('click', function () {
                // 按下的为class='q'举例
                // 更新button即可
                // 更换class
                button.className = this.className;
                // 更新图片
                if (svg_bri === null) {
                    svg_bri = getsvg_bright();
                }
                if (svg_dark === null) {
                    svg_dark = getsvg_dark();
                }
                let textElement = svg_bri.getElementById(button.id);
                textElement.textContent = this.className.toUpperCase();
                console.log(this.className);
                let textElement2 = svg_dark.getElementById(button.id);
                textElement2.textContent = this.className.toUpperCase();
                console.log(this.className);
                let serializer = new XMLSerializer();
                let svgStr = serializer.serializeToString(svg_bri);
                // 创建DataURL
                let dataUrl = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgStr);
                let serializer2 = new XMLSerializer();
                let svgStr_dark = serializer2.serializeToString(svg_dark);
                let dataUrl_dark = 'data:image/svg+xml; charset=utf8, ' + encodeURIComponent(svgStr_dark);
                if (document.body.style.backgroundColor === '') {
                    let img = document.querySelector('img.Img');
                    img.src = dataUrl;
                } else {
                    let img = document.querySelector('img.Img');
                    img.src = dataUrl_dark;
                }// 创建图像并设置其src属性

                // 覆写日月的onclick函数
                let changeBD = document.querySelector('img.sunANDmoon');
                let pictBD = document.querySelector('img.sunANDmoon_intouchbar');
                let handler = function () {
                    changeBD.style.opacity = 0;
                    pictBD.style.opacity = 0;
                    setTimeout(function () {
                        if (changeBD.getAttribute('src') === "./img/sun.svg") {
                            changeBD.setAttribute('src', './img/moon.svg');
                            pictBD.setAttribute('src', './img/moon.svg');
                            document.body.style.backgroundColor = "black";
                            let imag = document.querySelector("img.Img");
                            // imag.style.border = "2px solid wheat";
                            imag.src = dataUrl_dark;
                        }
                        else {
                            changeBD.setAttribute('src', './img/sun.svg');
                            pictBD.setAttribute('src', './img/sun.svg');
                            document.body.style.backgroundColor = '';
                            let imag = document.querySelector("img.Img");
                            // imag.style.border = "2px solid orangered";
                            imag.src = dataUrl;
                        }
                        changeBD.style.opacity = 1;
                        pictBD.style.opacity = 1;
                    }, 300);
                };
                changeBD.onclick = handler;
                pictBD.onclick = handler;
                // changeBD.removeEventListener('click', changeBDHandler);
            });
            map.appendChild(area);
        })(i);
    }
}

// 创建映射表
let keymap = {
    1: 'q', 2: 'w', 3: 'e', 4: 'r', 5: 't', 6: 'y', 7: 'u', 8: 'i', 9: 'o', 10: 'p',
    11: 'a', 12: 's', 13: 'd', 14: 'f', 15: 'g', 16: 'h', 17: 'j', 18: 'k', 19: 'l', 20: 'z',
    21: 'x', 22: 'c', 23: 'v', 24: 'b', 25: 'n', 26: 'm'
}

let keyincoords = {
    'q': 30, 'w': 31, "e": 32, "r": 33, "t": 34, "y": 35, "u": 36, "i": 37, "o": 38, "p": 39,
    'a': 44, 's': 45, 'd': 46, 'f': 47, 'g': 48, 'h': 49, 'j': 50, 'k': 51, 'l': 52,
    'z': 57, 'x': 58, 'c': 59, 'v': 60, 'b': 61, 'n': 62, 'm': 63
}

let ori_map = '<area shape="rect" coords="8,8,35,22" class="ESC" id="ESC" />' +
    '<area shape="rect" coords="38,8,65,22" class="F1" id="F1" />' +
    '<area shape="rect" coords="68,8,95,22" class="F2" id="F2" />' +
    '<area shape="rect" coords="98,8,125,22" class="F3" id="F3" />' +
    '<area shape="rect" coords="128,8,155,22" class="F4" id="F4" />' +
    '<area shape="rect" coords="158,8,185,22" class="F5" id="F5" />' +
    '<area shape="rect" coords="188,8,215,22" class="F6" id="F6" />' +
    '<area shape="rect" coords="218,8,245,22" class="F7" id="F7" />' +
    '<area shape="rect" coords="248,8,275,22" class="F8" id="F8" />' +
    '<area shape="rect" coords="278,8,305,22" class="F9" id="F9" />' +
    '<area shape="rect" coords="308,8,335,22" class="F10" id="F10" />' +
    '<area shape="rect" coords="338,8,365,22" class="F11" id="F11" />' +
    '<area shape="rect" coords="368,8,395,22" class="F12" id="F12" />' +
    '<area shape="rect" coords="398,8,423,22" class="DELETE" id="DELETE" />' +

    '<area coords="8,26,38,51" shape="rect" class="`"/>' +
    '<area coords="41,26,66,51" shape="rect" class="1"/>' +
    '<area coords="69,26,94,51" shape="rect" class="2"/>' +
    '<area coords="97,26,122,51" shape="rect" class="3"/>' +
    '<area coords="125,26,150,51" shape="rect" class="4"/>' +
    '<area coords="153,26,178,51" shape="rect" class="5"/>' +
    '<area coords="181,26,206,51" shape="rect" class="6"/>' +
    '<area coords="209,26,234,51" shape="rect" class="7"/>' +
    '<area coords="237,26,262,51" shape="rect" class="8"/>' +
    '<area coords="265,26,290,51" shape="rect" class="9"/>' +
    '<area coords="293,26,318,51" shape="rect" class="0"/>' +
    '<area coords="321,26,346,51" shape="rect" class="-"/>' +
    '<area coords="349,26,374,51" shape="rect" class="="/>' +
    '<area coords="377,26,423,51" shape="rect" class="BACK_SPACE"/>' +

    '<area coords="8,55,49,80" shape="rect" class="TAB"/>' +
    '<area coords="52,55,77,80" shape="rect" class="q"/>' +
    '<area coords="80,55,105,80" shape="rect" class="w"/>' +
    '<area coords="108,55,133,80" shape="rect" class="e"/>' +
    '<area coords="136,55,161,80" shape="rect" class="r"/>' +
    '<area coords="164,55,189,80" shape="rect" class="t"/>' +
    '<area coords="192,55,217,80" shape="rect" class="y"/>' +
    '<area coords="220,55,245,80" shape="rect" class="u"/>' +
    '<area coords="248,55,273,80" shape="rect" class="i"/>' +
    '<area coords="276,55,301,80" shape="rect" class="o"/>' +
    '<area coords="304,55,329,80" shape="rect" class="p"/>' +
    '<area coords="332,55,357,80" shape="rect" class="["/>' +
    '<area coords="360,55,385,80" shape="rect" class="]"/>' +
    '<area coords="388,55,423,80" shape="rect" class="\\"/>' +

    '<area coords="8,84,59,109" shape="rect" class="CAPS_LOCK"/>' +
    '<area coords="62,84,87,109" shape="rect" class="a"/>' +
    '<area coords="90,84,115,109" shape="rect" class="s"/>' +
    '<area coords="118,84,143,109" shape="rect" class="d"/>' +
    '<area coords="146,84,171,109" shape="rect" class="f"/>' +
    '<area coords="174,84,199,109" shape="rect" class="g"/>' +
    '<area coords="202,84,227,109" shape="rect" class="h"/>' +
    '<area coords="230,84,255,109" shape="rect" class="j"/>' +
    '<area coords="258,84,283,109" shape="rect" class="k"/>' +
    '<area coords="286,84,311,109" shape="rect" class="l"/>' +
    '<area coords="314,84,339,109" shape="rect" class=";"/>' +
    '<area coords="342,84,367,109" shape="rect" class="\'"/>' +
    '<area coords="370,84,423,109" shape="rect" class="enter"/>' +

    '<area coords="8,113,71,138" shape="rect" class="LEFT_SHIFT"/>' +
    '<area coords="74,113,99,138" shape="rect" class="z"/>' +
    '<area coords="102,113,127,138" shape="rect" class="x"/>' +
    '<area coords="130,113,155,138" shape="rect" class="c"/>' +
    '<area coords="158,113,183,138" shape="rect" class="v"/>' +
    '<area coords="186,113,211,138" shape="rect" class="b"/>' +
    '<area coords="214,113,239,138" shape="rect" class="n"/>' +
    '<area coords="242,113,267,138" shape="rect" class="m"/>' +
    '<area coords="270,113,295,138" shape="rect" class=","/>' +
    '<area coords="298,113,323,138" shape="rect" class="."/>' +
    '<area coords="326,113,351,138" shape="rect" class="/"/>' +
    '<area coords="354,113,423,138" shape="rect" class="RIGHT_SHIFT"/>' +

    '<area coords="8,142,36,167" shape="rect" class="LEFT_CTRL"/>' +
    '<area coords="39,142,64,167" shape="rect" class="FN"/>' +
    '<area coords="67,142,92,167" shape="rect" class="HOME"/>' +
    '<area coords="95,142,122,167" shape="rect" class="LEFT_ALT"/>' +
    '<area coords="125,142,271,167" shape="rect" class="SPACE"/>' +
    '<area coords="274,142,302,167" shape="rect" class="RIGHT_ALT"/>' +
    '<area coords="305,142,333,167" shape="rect" class="RIGHT_CTRL"/>' +
    '<area coords="336,153,363,167" shape="rect" class="KEYCODE_DPAD_LEFT"/>' +
    '<area coords="366,155,393,167" shape="rect" class="KEYCODE_DPAD_DOWN"/>' +
    '<area coords="366,142,393,154" shape="rect" class="KEYCODE_DPAD_UP"/>' +
    '<area coords="396,153,423,167" shape="rect" class="KEYCODE_DPAD_RIGHT"/>';