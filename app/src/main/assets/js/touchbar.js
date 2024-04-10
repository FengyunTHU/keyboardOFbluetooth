// touchbar
function settouchbar() {
    let oridiv = document.querySelector('div#main');
    oridiv.style.display = 'none';
    let divnew = document.querySelector('div#touchbar');
    divnew.style.display = 'flex';
    reset();
}

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

cous = 0;
function SETBluetooth_forbar() {
    if (cous === 0) {
        StartBluetooth_forbar();
        cous = 1;
    } else if (cous === 1) {
        deleteBle_forbar();
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