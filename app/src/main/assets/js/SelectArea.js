// 废弃
// 2024/03/23 22:03

function getPixel() {
    let Imgfs = document.querySelector("img.Img");
    let canvas = document.createElement('canvas');
    canvas.width = Imgfs.clientWidth;
    canvas.height = Imgfs.clientHeight;

    let ctx = canvas.getContext('2d');
    ctx.drawImage(Imgfs, 0, 0, Imgfs.clientWidth, Imgfs.clientHeight);

    let imageData = ctx.getImageData(0, 0, Imgfs.clientWidth, Imgfs.clientHeight).data;

    console.log(imageData);
}