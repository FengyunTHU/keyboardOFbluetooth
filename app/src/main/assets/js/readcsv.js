const myFrom = document.getElementById("myForm");
const csvFile = document.getElementById("csvFileInput");
let contentText;// 全局变量
window.lines;
// 处理字符串
function getposition() {
    lines = contentText.split('\n');
    for (var i = 0; i < lines.length; i++) {
        lines[i] = lines[i].replace(/\r+$/, '');
        lines[i] = lines[i].split(',');
    }
    console.log(lines);
}

myFrom.addEventListener("submit", function (e) {
    e.preventDefault();
    const input = csvFile.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
        contentText = e.target.result;
        console.log(contentText);
        getposition();
    };
    reader.readAsText(input);
})
