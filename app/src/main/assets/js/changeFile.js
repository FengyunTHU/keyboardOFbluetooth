// 导入文件所采用的js脚本
function setPictureOfKeyboard(base64Image, Type) {
    let img = document.querySelector("Img");
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


let lines_p = null;
function setPositionOfKeyboard(position) {
    lines_p = position.split('\n');
    for (var i = 0; i < lines_p.length; i++) {
        lines_p[i] = lines_p[i].replace(/\r+$/, '');
        lines_p[i] = lines_p[i].split(',');
    }
}