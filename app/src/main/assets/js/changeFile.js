// 导入文件所采用的js脚本
function setPictureOfKeyboard(base64Image, Type) {
    let img = document.querySelector("img.Img");
    let src = '';
    if (Type === "image/png") {
        src = 'data:image/png;base64,' + base64Image;
    }
    else if (Type === "image/jpeg") {
        src = 'data:image/jpeg;base64,' + base64Image;
    }
    else if (Type === "image/svg+xml") {
        src = 'data:image/svg+xml;base64,' + base64Image;
    }
    else if (Type === "image/gif") {
        src = 'data:image/gif;base64,' + base64Image;
    }
    else if (Type === "image/bmp") {
        src = 'data:image/bmp;base64,' + base64Image;
    }
    else if (Type === "image/webp") {
        src = 'data:image/webp;base64,' + base64Image;
    }
    else if (Type === "image/heif") {
        src = 'data:image/heif;base64,' + base64Image;
    }
    img.setAttribute("src", src);
}


function setPositionOfKeyboard(position) {
    let lines_p = null;
    lines_p = position.split('\n');
    for (var i = 0; i < lines_p.length; i++) {
        lines_p[i] = lines_p[i].replace(/\r+$/, '');
        lines_p[i] = lines_p[i].split(',');
    }
    // 处理位置信息
    let numberOfKey = lines_p[0][0];
    console.log(numberOfKey);
    let img = document.querySelector("img.Img");
    let wid_ori = img.naturalWidth;
    let newWid = img.clientWidth;
    let lines_originp = lines_p.slice(1);
    let chans = newWid / wid_ori;
    let map = document.getElementById("image-map");
    // 删除原元素
    while (map.firstChild) {
        map.removeChild(map.firstChild);
    }
    // 添加指定数量的元素
    for (var j = 0; j < numberOfKey; j++) {
        let area = document.createElement("area");
        map.appendChild(area);
    }
    let areasa = map.getElementsByTagName('area');
    if (lines_originp.length !== numberOfKey) {
        alert("设置键位数和提供数量不匹配!");
    }
    else {
        for (var k = 0; k < areasa.length; i++) {
            let area_nows = areasa[k];
            let area_origin = lines_originp[k];
            // 注意第一个是键位名称
            let coords_new = new Array(area_origin.length - 1);
            for (ii = 0; ii < coords_new.length; ii++) {
                coords_new[ii] = chans * area_origin[ii + 1];
            }
            areasa[k].setAttribute('coords', coords_new.join(','));
            // 添加shape+class
            areasa[k].setAttribute('class', area_origin[0]);
            if (area_origin.length === 4) {
                areasa[k].setAttribute('shape', 'circle');
            }
            else if (area_origin.length === 5) {
                areasa[k].setAttribute('shape', 'rect');
            }
            else if (area_origin.length >= 6) {
                areasa[k].setAttribute('shape', 'poly');
            }
        }
    }
}


function Showinformation(info) {
    let Infom = document.getElementById("info");
    let ptemp = document.createElement('p');
    ptemp.textContent = info;
    Infom.appendChild(ptemp);
    Infom.scrollTop = Infom.scrollHeight;
}