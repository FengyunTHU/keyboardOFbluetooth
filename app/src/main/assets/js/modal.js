let modal = document.getElementById("myModal");
let modalContent = document.getElementById("modal-content-pick");

// 隐藏窗口
window.onclick = function (event) {
    if (event.target === modal && event.target !== modalContent) {
        modal.style.transition = "opacity 0.2s, visibility 0s 0.2s";
        modal.style.opacity = "0";
        modal.style.visibility = 'hidden';
    }
}

// 显示窗口
function showModal() {
    // modalContent.style.top = top + 'px';
    // console.log(top);
    modal.style.transition = "opacity 0.2s, visibility 0.2s 0s";
    modal.style.opacity = "1";
    modal.style.visibility = "visible";
    chooseKEY();
}

// 选择键位
function chooseKEY() {
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
    for (i = 0; i < 45; i++) {
        (function (i) {
            // 依次添加按钮
            let area = document.createElement('area');
            area.shape = 'rect';
            area.coords = `${start_new + inter_new * i},${start_new},${start_new + inter_new * i + width_new},${start_new + width_new}`;
            area.className = keymap.get(i + 1);
            area.addEventListener('click', function () {

            });
            map.appendChild(area);
        })(i);
    }
}

// 创建映射表
let keymap = {
    1: '1',
    2: '2',
    3: '3',
    4: '4',
    5: '5',
    6: '6',
    7: '7',
    8: '8',
    9: '9',
    10: '0',
    11: '-',
    12: '=',
    13: 'q',
    14: 'w',
    15: 'e',
    16: 'r',
    17: 't',
    18: 'y',
    19: 'u',
    20: 'i',
    21: 'o',
    22: 'p',
    23: '[',
    24: ']',
    25: 'a',
    26: 's',
    27: 'd',
    28: 'f',
    29: 'g',
    30: 'h',
    31: 'j',
    32: 'k',
    33: 'l',
    34: ';',
    35: "'",
    36: 'z',
    37: 'x',
    38: 'c',
    39: 'v',
    40: 'b',
    41: 'n',
    42: 'm',
    43: ',',
    44: '.',
    45: '/'
}