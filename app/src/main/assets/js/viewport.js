function openNav() {
    document.getElementById("mySidebar").style.height = "50px";
    document.getElementById("main").style.marginTop = "50px";
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