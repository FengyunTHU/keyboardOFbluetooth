// let newdiv;
let test = document.querySelector("p.test");
// 这是检测按键功能的js脚本
function Addd(area) {
    test.textContent = test.textContent + area.className + " ";
}