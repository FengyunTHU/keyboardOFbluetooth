function openNav() {
    document.getElementById("mySidebar").style.height = "40px";
    document.getElementById("main").style.marginTop = "40px";
}

function closeNav() {
    document.getElementById("mySidebar").style.height = "0";
    document.getElementById("main").style.marginTop = "0";
}

function delayedRedirect(url, delay) {
    setTimeout(function () {
        window.location.href = url;
    }, delay);
}

function changepic() {
    let pict = document.querySelector('img.sunANDmoon');
    pict.style.opacity = 0;
    setTimeout(function () {
        if (pict.getAttribute('src') === "./img/sun.svg") {
            pict.setAttribute('src', './img/moon.svg');
            changeBODY_black();
        }
        else {
            pict.setAttribute('src', './img/sun.svg');
            changeBODY_ori();
        }
        pict.style.opacity = 1;
    }, 300);
}

function changeBODY_black() {
    document.body.style.backgroundColor = "black";
    let imag = document.querySelector("img.Img");
    imag.style.border = "2px solid wheat";
}

function changeBODY_ori() {
    document.body.style.backgroundColor = "";
    let imag = document.querySelector("img.Img");
    imag.style.border = "2px solid wheat";
}

// function changepic2() {
//     let pict = document.querySelector('.sunANDmoon');
//     if (pict.style.backgroundPosition === 'left center, right center') {
//         pict.style.backgroundPosition = 'right center, left center';
//     }
//     else {
//         pict.style.backgroundPosition = 'left center, right center';
//     }
// }