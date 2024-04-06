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
}

// 选择键位
function chooseKEY() {
    // 用图片来选择

}