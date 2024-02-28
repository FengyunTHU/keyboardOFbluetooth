// 导入文件所采用的js脚本
function setPictureOfKeyboard(base64Image, Type) {
    let img = document.createElement("img");
    if (Type === "image/png") {
        img.src = 'data:image/png;base64,' + base64Image;
    }
    else if (Type === "image/jpeg") {
        img.src = 'data:image/jpeg;base64,' + base64Image;
    }
    else if (Type === "image/svg+xml") {
        img.src = 'data:image/svg+xml;base64,' + base64Image;
    }
    else if (Type === "image/gif") {
        img.src = 'data:image/gif;base64,' + base64Image;
    }
    else if (Type === "image/bmp") {
        img.src = 'data:image/bmp;base64,' + base64Image;
    }
    else if (Type === "image/webp") {
        img.src = 'data:image/webp;base64,' + base64Image;
    }
    else if (Type === "image/heif") {
        img.src = 'data:image/heif;base64,' + base64Image;
    }
    document.body.appendChild(img);
}